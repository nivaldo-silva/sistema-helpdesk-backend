package io.github.nivaldosilva.helpdesk.chamado;

import io.github.nivaldosilva.helpdesk.chamado.enums.Prioridade;
import io.github.nivaldosilva.helpdesk.chamado.enums.Status;
import io.github.nivaldosilva.helpdesk.chamado.validation.ChamadoValidator;
import io.github.nivaldosilva.helpdesk.core.exception.RegistroNaoEncontradoException;
import io.github.nivaldosilva.helpdesk.usuario.Usuario;
import io.github.nivaldosilva.helpdesk.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChamadoService {

	private final ChamadoRepository chamadoRepository;
	private final UsuarioRepository usuarioRepository;
	private final ChamadoValidator chamadoValidator;

	@Transactional
	public Chamado abrir(ChamadoDTO.Request request, UUID usuarioId) {
		Usuario solicitante = buscarUsuario(usuarioId);
		chamadoValidator.validarPodeAbrir(solicitante);

		Chamado chamado = chamadoRepository.save(
				ChamadoMapper.toEntity(request, solicitante.getEmpresa(), solicitante));

		log.info("Chamado {} aberto por {}", chamado.getId(), usuarioId);
		return chamado;
	}

	@Transactional(readOnly = true)
	public Chamado buscarPorId(UUID chamadoId, UUID usuarioId) {
		return buscarChamadoVisivel(chamadoId, buscarUsuario(usuarioId));
	}

	@Transactional(readOnly = true)
	public Page<Chamado> listar(Specification<Chamado> filtros, Pageable pageable, UUID usuarioId) {
		Usuario usuario = buscarUsuario(usuarioId);
		return chamadoRepository.findAll(visivelPara(usuario).and(filtros), pageable);
	}

	@Transactional(readOnly = true)
	public Page<Chamado> listarAbertos(Pageable pageable, UUID usuarioId) {
		chamadoValidator.validarEquipeDeSuporte(buscarUsuario(usuarioId));
		return chamadoRepository.findAllByStatus(Status.ABERTO, pageable);
	}

	@Transactional
	public Chamado atualizar(UUID chamadoId, ChamadoDTO.Request request, UUID usuarioId) {
		Usuario usuario = buscarUsuario(usuarioId);
		Chamado chamado = buscarChamadoVisivel(chamadoId, usuario);
		chamadoValidator.validarPodeEditar(usuario, chamado);

		chamado.atualizarDados(request.titulo(), request.descricao(), request.categoria(), request.setor());

		log.info("Chamado {} atualizado por {}", chamadoId, usuarioId);
		return chamado;
	}

	@Transactional
	public Chamado aceitar(UUID chamadoId, UUID usuarioId) {
		Usuario atendente = buscarUsuario(usuarioId);
		chamadoValidator.validarEquipeDeSuporte(atendente);

		Chamado chamado = buscarChamadoVisivel(chamadoId, atendente);
		chamadoValidator.validarPodeAceitar(chamado);
		chamado.atribuirResponsavel(atendente);

		log.info("Chamado {} aceito por {}", chamadoId, usuarioId);
		return chamado;
	}

	@Transactional
	public Chamado alterarStatus(UUID chamadoId, Status novoStatus, UUID usuarioId) {
		Usuario usuario = buscarUsuario(usuarioId);
		Chamado chamado = buscarChamadoVisivel(chamadoId, usuario);
		chamadoValidator.validarAlteracaoDeStatus(usuario, chamado, novoStatus);

		Status statusAnterior = chamado.getStatus();
		LocalDateTime agora = LocalDateTime.now();

		switch (novoStatus) {
			case EM_ANDAMENTO -> chamado.reabrir();
			case RESOLVIDO -> chamado.resolver(agora);
			case FECHADO -> chamado.fechar(agora);
			case CANCELADO -> chamado.cancelar(agora);
			default -> { }
		}

		log.info("Chamado {} alterado de {} para {} por {}", chamadoId, statusAnterior, novoStatus, usuarioId);
		return chamado;
	}

	@Transactional
	public Chamado alterarPrioridade(UUID chamadoId, Prioridade novaPrioridade, UUID usuarioId) {
		Usuario usuario = buscarUsuario(usuarioId);
		Chamado chamado = buscarChamadoVisivel(chamadoId, usuario);
		chamadoValidator.validarPodeGerenciar(usuario, chamado);

		Prioridade prioridadeAnterior = chamado.getPrioridade();
		chamado.alterarPrioridade(novaPrioridade);

		log.info("Prioridade do chamado {} alterada de {} para {} por {}",
				chamadoId, prioridadeAnterior, novaPrioridade, usuarioId);
		return chamado;
	}

	@Transactional
	public void excluir(UUID chamadoId, UUID usuarioId) {
		Usuario usuario = buscarUsuario(usuarioId);
		chamadoValidator.validarAdministrador(usuario);

		Chamado chamado = buscarChamadoVisivel(chamadoId, usuario);
		chamadoValidator.validarPodeExcluir(chamado);
		chamado.desativar();

		log.info("Chamado {} desativado por {}", chamadoId, usuarioId);
	}

	private Usuario buscarUsuario(UUID usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RegistroNaoEncontradoException("Usuário não encontrado"));
	}

	private Chamado buscarChamadoVisivel(UUID chamadoId, Usuario usuario) {
		Specification<Chamado> doChamado = (root, query, builder) -> builder.equal(root.get("id"), chamadoId);
		return chamadoRepository.findOne(visivelPara(usuario).and(doChamado))
				.orElseThrow(() -> new RegistroNaoEncontradoException("Chamado não encontrado"));
	}

	private Specification<Chamado> visivelPara(Usuario usuario) {
		if (usuario.getPerfil().equipeDeSuporte()) {
			return (root, query, builder) -> builder.conjunction();
		}
		return (root, query, builder) -> builder.and(
				builder.equal(root.get("empresa").get("id"), usuario.getEmpresa().getId()),
				builder.equal(root.get("solicitante").get("id"), usuario.getId()));
	}
}