package io.github.nivaldosilva.helpdesk.modules.comentario;

import io.github.nivaldosilva.helpdesk.modules.chamado.ChamadoService;
import io.github.nivaldosilva.helpdesk.common.exception.AcessoNegadoAoRecursoException;
import io.github.nivaldosilva.helpdesk.common.exception.RegistroNaoEncontradoException;
import io.github.nivaldosilva.helpdesk.modules.usuario.Usuario;
import io.github.nivaldosilva.helpdesk.modules.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComentarioService {

	private final ComentarioRepository comentarioRepository;
	private final UsuarioRepository usuarioRepository;
	private final ChamadoService chamadoService;

	@Transactional
	public ComentarioDTO.Response adicionar(UUID chamadoId, ComentarioDTO.Request request, UUID usuarioId) {
		Usuario autor = buscarUsuario(usuarioId);
		validarAcessoAoChamado(chamadoId, usuarioId);

		if (request.notaInterna() && !autor.getPerfil().equipeDeSuporte()) {
			throw new AcessoNegadoAoRecursoException("Apenas a equipe de suporte pode adicionar notas internas");
		}

		Comentario salvo = comentarioRepository.save(ComentarioMapper.toEntity(request, chamadoId, autor));
		log.info("Comentário {} adicionado ao chamado {} por {} (nota interna: {})",
				salvo.getId(), chamadoId, usuarioId, request.notaInterna());
		return ComentarioMapper.toResponse(salvo);
	}

	@Transactional(readOnly = true)
	public Page<ComentarioDTO.Response> listar(UUID chamadoId, Pageable pageable, UUID usuarioId) {
		Usuario usuario = buscarUsuario(usuarioId);
		validarAcessoAoChamado(chamadoId, usuarioId);

		Page<Comentario> comentarios = usuario.getPerfil().equipeDeSuporte()
				? comentarioRepository.findAllByChamadoIdOrderByCriadoEmAsc(chamadoId, pageable)
				: comentarioRepository.findAllByChamadoIdAndNotaInternaFalseOrderByCriadoEmAsc(chamadoId, pageable);

		return comentarios.map(ComentarioMapper::toResponse);
	}

	private void validarAcessoAoChamado(UUID chamadoId, UUID usuarioId) {
		chamadoService.buscarPorId(chamadoId, usuarioId);
	}

	private Usuario buscarUsuario(UUID usuarioId) {
		return usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new RegistroNaoEncontradoException("Usuário não encontrado"));
	}
}