package io.github.nivaldosilva.helpdesk.usuario;

import io.github.nivaldosilva.helpdesk.usuario.enums.Perfil;
import io.github.nivaldosilva.helpdesk.usuario.spec.UsuarioSpec;
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

@Tag(name = "Usuários", description = "Cadastro, consulta, atualização e desativação de usuários. Perfis: ADMIN, SUPORTE e CLIENTE.")
@SecurityRequirement(name = "bearerAuth")
public interface UsuarioDocs {

	@Operation(summary = "Cadastrar cliente",
			description = "Cria um usuário CLIENTE. Um cliente autenticado cadastra na própria empresa; "
					+ "ADMIN e SUPORTE informam `empresa_id`, que deve ser de uma empresa ativa.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Cliente criado"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos, e-mail em uso ou empresa inativa"),
			@ApiResponse(responseCode = "404", description = "Empresa não encontrada")
	})
	ResponseEntity<UsuarioDTO.Response> criarCliente(
			UsuarioDTO.Request request,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Cadastrar colaborador",
			description = "Somente ADMIN. Cria um usuário ADMIN ou SUPORTE, que não é vinculado a empresa.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Colaborador criado"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos, e-mail em uso ou perfil diferente de ADMIN/SUPORTE"),
			@ApiResponse(responseCode = "403", description = "Acesso restrito a administradores")
	})
	ResponseEntity<UsuarioDTO.Response> criarColaborador(
			UsuarioDTO.Request request,
			@Parameter(description = "Perfil do colaborador: ADMIN ou SUPORTE", required = true) Perfil perfil);

	@Operation(summary = "Buscar usuário por ID",
			description = "ADMIN e SUPORTE consultam qualquer usuário. Os demais consultam apenas a própria conta; "
					+ "qualquer outro ID retorna 404.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Usuário encontrado"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	ResponseEntity<UsuarioDTO.Response> buscarPorId(
			@Parameter(description = "Identificador do usuário (UUID)", required = true) UUID id,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Listar usuários",
			description = "ADMIN e SUPORTE. Filtros opcionais e paginação (padrão: 10 por página, ordenados por nome).")
	ResponseEntity<Page<UsuarioDTO.Response>> listar(
			UsuarioSpec spec,
			@ParameterObject Pageable pageable);

	@Operation(summary = "Atualizar usuário",
			description = "Atualiza nome, e-mail e telefone. Permitido ao próprio usuário ou a um ADMIN. "
					+ "A senha é opcional e só pode ser alterada pelo próprio usuário. A empresa não pode ser alterada.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Usuário atualizado"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos ou e-mail em uso"),
			@ApiResponse(responseCode = "403", description = "Sem permissão para alterar este usuário ou sua senha"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	ResponseEntity<UsuarioDTO.Response> atualizar(
			@Parameter(description = "Identificador do usuário (UUID)", required = true) UUID id,
			UsuarioDTO.AtualizacaoRequest request,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Desativar usuário",
			description = "Somente ADMIN. O usuário deixa de conseguir autenticar, mas o cadastro e o histórico são mantidos. "
					+ "Não é possível desativar a própria conta.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Usuário desativado"),
			@ApiResponse(responseCode = "400", description = "Tentativa de desativar a própria conta"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	ResponseEntity<Void> desativar(
			@Parameter(description = "Identificador do usuário (UUID)", required = true) UUID id,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Reativar usuário", description = "Somente ADMIN. Devolve o acesso a um usuário desativado.")
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Usuário reativado"),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado")
	})
	ResponseEntity<Void> reativar(
			@Parameter(description = "Identificador do usuário (UUID)", required = true) UUID id);
}