package io.github.nivaldosilva.helpdesk.core.security;

import io.github.nivaldosilva.helpdesk.usuario.UsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Autenticação", description = "Login e registro público de clientes. Endpoints que não exigem token.")
public interface AutenticacaoDocs {

	@Operation(summary = "Autenticar e gerar token JWT",
			description = "Público. Valida as credenciais e retorna o token JWT, que deve ser enviado no cabeçalho "
					+ "`Authorization: Bearer <token>` nas demais requisições. Usuários desativados não conseguem autenticar.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Autenticado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "401", description = "Credenciais inválidas ou usuário desativado")
	})
	ResponseEntity<Credenciais.Response> login(Credenciais.Request request);

	@Operation(summary = "Registrar cliente",
			description = "Público. Cria um usuário com perfil CLIENTE, vinculado a uma empresa ativa informada em `empresa_id`.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Cliente registrado"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos, e-mail em uso ou empresa inativa"),
			@ApiResponse(responseCode = "404", description = "Empresa não encontrada")
	})
	ResponseEntity<UsuarioDTO.Response> registrar(UsuarioDTO.Request request);
}