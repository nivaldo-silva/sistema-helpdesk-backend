package io.github.nivaldosilva.helpdesk.chamado;

import io.github.nivaldosilva.helpdesk.chamado.enums.Prioridade;
import io.github.nivaldosilva.helpdesk.chamado.enums.Status;
import io.github.nivaldosilva.helpdesk.chamado.spec.ChamadoSpec;
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
@RequestMapping("/chamados")
@RequiredArgsConstructor
public class ChamadoController implements ChamadoDocs {

	private static final String PERMISSAO_ABRIR = "hasRole('CLIENTE')";
	private static final String PERMISSAO_BUSCAR_POR_ID = "isAuthenticated()";
	private static final String PERMISSAO_LISTAR = "isAuthenticated()";
	private static final String PERMISSAO_LISTAR_FILA_DE_ABERTOS = "hasAnyRole('ADMIN', 'SUPORTE')";
	private static final String PERMISSAO_ATUALIZAR = "isAuthenticated()";
	private static final String PERMISSAO_ACEITAR = "hasAnyRole('ADMIN', 'SUPORTE')";
	private static final String PERMISSAO_ALTERAR_STATUS = "hasAnyRole('ADMIN', 'SUPORTE')";
	private static final String PERMISSAO_ALTERAR_PRIORIDADE = "hasAnyRole('ADMIN', 'SUPORTE')";
	private static final String PERMISSAO_EXCLUIR = "hasRole('ADMIN')";

	private final ChamadoService chamadoService;

	@PostMapping
	@PreAuthorize(PERMISSAO_ABRIR)
	public ResponseEntity<ChamadoDTO.Response> abrir(
			@Valid @RequestBody ChamadoDTO.Request request,
			@AuthenticationPrincipal UUID usuarioId) {
		Chamado chamado = chamadoService.abrir(request, usuarioId);
		return ResponseEntity.status(HttpStatus.CREATED).body(ChamadoMapper.toResponse(chamado));
	}

	@GetMapping("/{id}")
	@PreAuthorize(PERMISSAO_BUSCAR_POR_ID)
	public ResponseEntity<ChamadoDTO.Response> buscarPorId(
			@PathVariable UUID id,
			@AuthenticationPrincipal UUID usuarioId) {
		Chamado chamado = chamadoService.buscarPorId(id, usuarioId);
		return ResponseEntity.ok(ChamadoMapper.toResponse(chamado));
	}

	@GetMapping
	@PreAuthorize(PERMISSAO_LISTAR)
	public ResponseEntity<Page<ChamadoDTO.Response>> listar(
			ChamadoSpec spec,
			@PageableDefault(size = 10, sort = "criadoEm") Pageable pageable,
			@AuthenticationPrincipal UUID usuarioId) {
		Page<Chamado> chamados = chamadoService.listar(spec, pageable, usuarioId);
		return ResponseEntity.ok(chamados.map(ChamadoMapper::toResponse));
	}

	@GetMapping("/abertos")
	@PreAuthorize(PERMISSAO_LISTAR_FILA_DE_ABERTOS)
	public ResponseEntity<Page<ChamadoDTO.Response>> listarAbertos(
			@PageableDefault(size = 10, sort = "criadoEm") Pageable pageable,
			@AuthenticationPrincipal UUID usuarioId) {
		Page<Chamado> chamados = chamadoService.listarAbertos(pageable, usuarioId);
		return ResponseEntity.ok(chamados.map(ChamadoMapper::toResponse));
	}

	@PutMapping("/{id}")
	@PreAuthorize(PERMISSAO_ATUALIZAR)
	public ResponseEntity<ChamadoDTO.Response> atualizar(
			@PathVariable UUID id,
			@Valid @RequestBody ChamadoDTO.Request request,
			@AuthenticationPrincipal UUID usuarioId) {
		Chamado chamado = chamadoService.atualizar(id, request, usuarioId);
		return ResponseEntity.ok(ChamadoMapper.toResponse(chamado));
	}

	@PatchMapping("/{id}/aceitar")
	@PreAuthorize(PERMISSAO_ACEITAR)
	public ResponseEntity<ChamadoDTO.Response> aceitar(
			@PathVariable UUID id,
			@AuthenticationPrincipal UUID usuarioId) {
		Chamado chamado = chamadoService.aceitar(id, usuarioId);
		return ResponseEntity.ok(ChamadoMapper.toResponse(chamado));
	}

	@PatchMapping("/{id}/status")
	@PreAuthorize(PERMISSAO_ALTERAR_STATUS)
	public ResponseEntity<ChamadoDTO.Response> alterarStatus(
			@PathVariable UUID id,
			@RequestParam Status status,
			@AuthenticationPrincipal UUID usuarioId) {
		Chamado chamado = chamadoService.alterarStatus(id, status, usuarioId);
		return ResponseEntity.ok(ChamadoMapper.toResponse(chamado));
	}

	@PatchMapping("/{id}/prioridade")
	@PreAuthorize(PERMISSAO_ALTERAR_PRIORIDADE)
	public ResponseEntity<ChamadoDTO.Response> alterarPrioridade(
			@PathVariable UUID id,
			@RequestParam Prioridade prioridade,
			@AuthenticationPrincipal UUID usuarioId) {
		Chamado chamado = chamadoService.alterarPrioridade(id, prioridade, usuarioId);
		return ResponseEntity.ok(ChamadoMapper.toResponse(chamado));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize(PERMISSAO_EXCLUIR)
	public ResponseEntity<Void> excluir(
			@PathVariable UUID id,
			@AuthenticationPrincipal UUID usuarioId) {
		chamadoService.excluir(id, usuarioId);
		return ResponseEntity.noContent().build();
	}
}