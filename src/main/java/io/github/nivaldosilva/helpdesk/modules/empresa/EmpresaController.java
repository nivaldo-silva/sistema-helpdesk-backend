package io.github.nivaldosilva.helpdesk.modules.empresa;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresaController implements EmpresaDocs {

	private static final String PERMISSAO_CRIAR = "hasRole('ADMIN')";
	private static final String PERMISSAO_BUSCAR_POR_ID = "hasAnyRole('ADMIN', 'SUPORTE')";
	private static final String PERMISSAO_LISTAR = "hasAnyRole('ADMIN', 'SUPORTE')";
	private static final String PERMISSAO_ATUALIZAR = "hasRole('ADMIN')";
	private static final String PERMISSAO_DESATIVAR = "hasRole('ADMIN')";
	private static final String PERMISSAO_REATIVAR = "hasRole('ADMIN')";

	private final EmpresaService empresaService;

	@PostMapping
	@PreAuthorize(PERMISSAO_CRIAR)
	public ResponseEntity<EmpresaDTO.Response> criar(@Valid @RequestBody EmpresaDTO.Request request) {
		Empresa empresa = empresaService.criar(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(EmpresaMapper.toResponse(empresa));
	}

	@GetMapping("/{id}")
	@PreAuthorize(PERMISSAO_BUSCAR_POR_ID)
	public ResponseEntity<EmpresaDTO.Response> buscarPorId(@PathVariable UUID id) {
		return ResponseEntity.ok(EmpresaMapper.toResponse(empresaService.buscarPorId(id)));
	}

	@GetMapping
	@PreAuthorize(PERMISSAO_LISTAR)
	public ResponseEntity<Page<EmpresaDTO.Response>> listar(
			@PageableDefault(size = 10, sort = "nome") Pageable pageable) {
		return ResponseEntity.ok(empresaService.listar(pageable).map(EmpresaMapper::toResponse));
	}

	@PutMapping("/{id}")
	@PreAuthorize(PERMISSAO_ATUALIZAR)
	public ResponseEntity<EmpresaDTO.Response> atualizar(
			@PathVariable UUID id,
			@Valid @RequestBody EmpresaDTO.AtualizacaoRequest request) {
		return ResponseEntity.ok(EmpresaMapper.toResponse(empresaService.atualizar(id, request)));
	}

	@PatchMapping("/{id}/desativar")
	@PreAuthorize(PERMISSAO_DESATIVAR)
	public ResponseEntity<Void> desativar(@PathVariable UUID id) {
		empresaService.desativar(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/reativar")
	@PreAuthorize(PERMISSAO_REATIVAR)
	public ResponseEntity<Void> reativar(@PathVariable UUID id) {
		empresaService.reativar(id);
		return ResponseEntity.noContent().build();
	}
}