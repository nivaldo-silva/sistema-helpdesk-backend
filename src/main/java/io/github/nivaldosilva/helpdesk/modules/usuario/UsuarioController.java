package io.github.nivaldosilva.helpdesk.modules.usuario;

import io.github.nivaldosilva.helpdesk.modules.usuario.enums.Perfil;
import io.github.nivaldosilva.helpdesk.modules.usuario.spec.UsuarioSpec;
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
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController implements UsuarioDocs {

	private static final String PERMISSAO_CRIAR_CLIENTE = "isAuthenticated()";
	private static final String PERMISSAO_CRIAR_COLABORADOR = "hasRole('ADMIN')";
	private static final String PERMISSAO_BUSCAR_POR_ID = "isAuthenticated()";
	private static final String PERMISSAO_LISTAR = "hasAnyRole('ADMIN', 'SUPORTE')";
	private static final String PERMISSAO_ATUALIZAR = "isAuthenticated()";
	private static final String PERMISSAO_DESATIVAR = "hasRole('ADMIN')";
	private static final String PERMISSAO_REATIVAR = "hasRole('ADMIN')";

	private final UsuarioService usuarioService;

	@PostMapping("/clientes")
	@PreAuthorize(PERMISSAO_CRIAR_CLIENTE)
	public ResponseEntity<UsuarioDTO.Response> criarCliente(
			@Valid @RequestBody UsuarioDTO.Request request,
			@AuthenticationPrincipal UUID usuarioId) {
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criarCliente(request, usuarioId));
	}

	@PostMapping("/admin/colaboradores")
	@PreAuthorize(PERMISSAO_CRIAR_COLABORADOR)
	public ResponseEntity<UsuarioDTO.Response> criarColaborador(
			@Valid @RequestBody UsuarioDTO.Request request,
			@RequestParam Perfil perfil) {
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criarColaborador(request, perfil));
	}

	@GetMapping("/{id}")
	@PreAuthorize(PERMISSAO_BUSCAR_POR_ID)
	public ResponseEntity<UsuarioDTO.Response> buscarPorId(
			@PathVariable UUID id,
			@AuthenticationPrincipal UUID usuarioId) {
		return ResponseEntity.ok(usuarioService.buscarPorId(id, usuarioId));
	}

	@GetMapping
	@PreAuthorize(PERMISSAO_LISTAR)
	public ResponseEntity<Page<UsuarioDTO.Response>> listar(
			UsuarioSpec spec,
			@PageableDefault(size = 10, sort = "nome") Pageable pageable) {
		return ResponseEntity.ok(usuarioService.listar(spec, pageable));
	}

	@PutMapping("/{id}")
	@PreAuthorize(PERMISSAO_ATUALIZAR)
	public ResponseEntity<UsuarioDTO.Response> atualizar(
			@PathVariable UUID id,
			@Valid @RequestBody UsuarioDTO.AtualizacaoRequest request,
			@AuthenticationPrincipal UUID usuarioId) {
		return ResponseEntity.ok(usuarioService.atualizar(id, request, usuarioId));
	}

	@PatchMapping("/{id}/desativar")
	@PreAuthorize(PERMISSAO_DESATIVAR)
	public ResponseEntity<Void> desativar(
			@PathVariable UUID id,
			@AuthenticationPrincipal UUID usuarioId) {
		usuarioService.desativar(id, usuarioId);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/reativar")
	@PreAuthorize(PERMISSAO_REATIVAR)
	public ResponseEntity<Void> reativar(@PathVariable UUID id) {
		usuarioService.reativar(id);
		return ResponseEntity.noContent().build();
	}
}