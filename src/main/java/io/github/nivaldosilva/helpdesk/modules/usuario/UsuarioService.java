package io.github.nivaldosilva.helpdesk.modules.usuario;

import io.github.nivaldosilva.helpdesk.common.exception.AcessoNegadoAoRecursoException;
import io.github.nivaldosilva.helpdesk.common.exception.OperacaoNaoPermitidaException;
import io.github.nivaldosilva.helpdesk.common.exception.RegistroNaoEncontradoException;
import io.github.nivaldosilva.helpdesk.modules.empresa.Empresa;
import io.github.nivaldosilva.helpdesk.modules.empresa.EmpresaRepository;
import io.github.nivaldosilva.helpdesk.modules.usuario.enums.Perfil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;
	private final EmpresaRepository empresaRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return usuarioRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
	}

	@Transactional
	public UsuarioDTO.Response criarCliente(UsuarioDTO.Request request, UUID criadorId) {
		Empresa empresa = buscarEmpresaAtivaDoCliente(request, criadorId);
		return salvar(request, Perfil.CLIENTE, empresa);
	}

	@Transactional
	public UsuarioDTO.Response criarColaborador(UsuarioDTO.Request request, Perfil perfil) {
		if (!perfil.equipeDeSuporte()) {
			throw new OperacaoNaoPermitidaException("Colaboradores devem ter o perfil ADMIN ou SUPORTE.");
		}
		return salvar(request, perfil, null);
	}

	@Transactional(readOnly = true)
	public UsuarioDTO.Response buscarPorId(UUID id, UUID usuarioLogadoId) {
		Usuario logado = buscarUsuario(usuarioLogadoId);
		boolean podeConsultar = logado.getPerfil().equipeDeSuporte() || logado.getId().equals(id);
		if (!podeConsultar) {
			throw new RegistroNaoEncontradoException("Usuário não encontrado.");
		}
		return UsuarioMapper.toResponse(buscarUsuario(id));
	}

	@Transactional(readOnly = true)
	public Page<UsuarioDTO.Response> listar(Specification<Usuario> filtros, Pageable pageable) {
		return usuarioRepository.findAll(filtros, pageable).map(UsuarioMapper::toResponse);
	}

	@Transactional
	public UsuarioDTO.Response atualizar(UUID id, UsuarioDTO.AtualizacaoRequest request, UUID usuarioLogadoId) {
		Usuario logado = buscarUsuario(usuarioLogadoId);
		boolean propriaConta = logado.getId().equals(id);

		if (!propriaConta && logado.getPerfil() != Perfil.ADMIN) {
			throw new AcessoNegadoAoRecursoException("Somente o próprio usuário ou um administrador pode alterar estes dados.");
		}
		if (request.senha() != null && !propriaConta) {
			throw new AcessoNegadoAoRecursoException("A senha só pode ser alterada pelo próprio usuário.");
		}

		String email = UsuarioMapper.normalizarEmail(request.email());
		if (usuarioRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
			throw new OperacaoNaoPermitidaException("Este e-mail já está sendo utilizado por outro usuário.");
		}

		Usuario usuario = propriaConta ? logado : buscarUsuario(id);
		usuario.atualizarDados(request.nome().trim(), email, request.telefone());
		if (request.senha() != null) {
			usuario.atualizarSenha(passwordEncoder.encode(request.senha()));
		}

		log.info("Usuário {} atualizado por {}", id, usuarioLogadoId);
		return UsuarioMapper.toResponse(usuario);
	}

	@Transactional
	public void desativar(UUID id, UUID usuarioLogadoId) {
		if (id.equals(usuarioLogadoId)) {
			throw new OperacaoNaoPermitidaException("Você não pode desativar a própria conta.");
		}
		buscarUsuario(id).desativar();
		log.info("Usuário {} desativado por {}", id, usuarioLogadoId);
	}

	@Transactional
	public void reativar(UUID id) {
		buscarUsuario(id).reativar();
		log.info("Usuário {} reativado", id);
	}

	@Transactional
	public void registrarAcesso(UUID usuarioId) {
		usuarioRepository.findById(usuarioId).ifPresent(usuario -> {
			LocalDateTime limite = LocalDateTime.now().minusMinutes(10);
			if (usuario.getUltimoAcesso() == null || usuario.getUltimoAcesso().isBefore(limite)) {
				usuario.registrarAcesso();
			}
		});
	}

	private UsuarioDTO.Response salvar(UsuarioDTO.Request request, Perfil perfil, Empresa empresa) {
		String email = UsuarioMapper.normalizarEmail(request.email());
		if (usuarioRepository.existsByEmailIgnoreCase(email)) {
			throw new OperacaoNaoPermitidaException("Este e-mail já está sendo utilizado por outro usuário.");
		}

		String senhaCodificada = passwordEncoder.encode(request.senha());
		Usuario salvo = usuarioRepository.save(UsuarioMapper.toEntity(request, senhaCodificada, empresa, perfil));

		log.info("Usuário {} criado com o perfil {}", salvo.getId(), perfil);
		return UsuarioMapper.toResponse(salvo);
	}

	private Empresa buscarEmpresaAtivaDoCliente(UsuarioDTO.Request request, UUID criadorId) {
		UUID empresaId = request.empresaId();

		if (criadorId != null) {
			Usuario criador = buscarUsuario(criadorId);
			if (criador.getPerfil() == Perfil.CLIENTE) {
				empresaId = criador.getEmpresa().getId();
			}
		}
		if (empresaId == null) {
			throw new OperacaoNaoPermitidaException("Informe a empresa do cliente (empresa_id).");
		}

		Empresa empresa = empresaRepository.findById(empresaId)
				.orElseThrow(() -> new RegistroNaoEncontradoException("Empresa não encontrada."));
		if (!empresa.isAtivo()) {
			throw new OperacaoNaoPermitidaException("A empresa informada está inativa.");
		}
		return empresa;
	}

	private Usuario buscarUsuario(UUID id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new RegistroNaoEncontradoException("Usuário não encontrado."));
	}
}