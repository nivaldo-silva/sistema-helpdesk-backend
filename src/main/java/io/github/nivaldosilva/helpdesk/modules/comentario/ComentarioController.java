package io.github.nivaldosilva.helpdesk.modules.comentario;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/chamados/{chamadoId}/comentarios")
@RequiredArgsConstructor
public class ComentarioController implements ComentarioDocs {

	private static final String PERMISSAO_ADICIONAR = "isAuthenticated()";
	private static final String PERMISSAO_LISTAR = "isAuthenticated()";

	private final ComentarioService comentarioService;

	@PostMapping
	@PreAuthorize(PERMISSAO_ADICIONAR)
	public ResponseEntity<ComentarioDTO.Response> adicionar(
			@PathVariable UUID chamadoId,
			@Valid @RequestBody ComentarioDTO.Request request,
			@AuthenticationPrincipal UUID usuarioId) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(comentarioService.adicionar(chamadoId, request, usuarioId));
	}

	@GetMapping
	@PreAuthorize(PERMISSAO_LISTAR)
	public ResponseEntity<Page<ComentarioDTO.Response>> listar(
			@PathVariable UUID chamadoId,
			@PageableDefault(size = 20) Pageable pageable,
			@AuthenticationPrincipal UUID usuarioId) {
		return ResponseEntity.ok(comentarioService.listar(chamadoId, pageable, usuarioId));
	}
}