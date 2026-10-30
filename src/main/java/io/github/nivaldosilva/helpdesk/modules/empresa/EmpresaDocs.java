package io.github.nivaldosilva.helpdesk.modules.empresa;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

@Tag(name = "Empresas", description = "Cadastro e gerenciamento das empresas clientes")
@SecurityRequirement(name = "bearerAuth")
public interface EmpresaDocs {

	@Operation(summary = "Cadastrar empresa",
			description = "Somente ADMIN. O CNPJ deve ter 14 caracteres, sem pontuação (12 letras maiúsculas ou números "
					+ "seguidos de 2 dígitos), e não pode ser alterado depois.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Empresa cadastrada"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos ou CNPJ já cadastrado"),
			@ApiResponse(responseCode = "403", description = "Acesso restrito a administradores")
	})
	ResponseEntity<EmpresaDTO.Response> criar(EmpresaDTO.Request request);

	@Operation(summary = "Buscar empresa por ID", description = "ADMIN e SUPORTE.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Empresa encontrada"),
			@ApiResponse(responseCode = "404", description = "Empresa não encontrada")
	})
	ResponseEntity<EmpresaDTO.Response> buscarPorId(
			@Parameter(description = "Identificador da empresa (UUID)", required = true) UUID id);

	@Operation(summary = "Listar empresas",
			description = "ADMIN e SUPORTE. Inclui empresas inativas. Padrão: 10 por página, ordenadas por nome.")
	ResponseEntity<Page<EmpresaDTO.Response>> listar(@ParameterObject Pageable pageable);

	@Operation(summary = "Atualizar empresa",
			description = "Somente ADMIN. Altera nome, e-mail corporativo e telefone. O CNPJ não pode ser alterado.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Empresa atualizada"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "404", description = "Empresa não encontrada")
	})
	ResponseEntity<EmpresaDTO.Response> atualizar(
			@Parameter(description = "Identificador da empresa (UUID)", required = true) UUID id,
			EmpresaDTO.AtualizacaoRequest request);

	@Operation(summary = "Desativar empresa",
			description = "Somente ADMIN. A empresa deixa de poder abrir chamados e cadastrar clientes; o histórico é mantido.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Empresa desativada"),
			@ApiResponse(responseCode = "404", description = "Empresa não encontrada")
	})
	ResponseEntity<Void> desativar(
			@Parameter(description = "Identificador da empresa (UUID)", required = true) UUID id);

	@Operation(summary = "Reativar empresa", description = "Somente ADMIN.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Empresa reativada"),
			@ApiResponse(responseCode = "404", description = "Empresa não encontrada")
	})
	ResponseEntity<Void> reativar(
			@Parameter(description = "Identificador da empresa (UUID)", required = true) UUID id);
}