package io.github.nivaldosilva.helpdesk.modules.usuario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.helpdesk.modules.usuario.enums.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

public final class UsuarioDTO {

	private UsuarioDTO() {}

	private static final String REGRA_SENHA = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,72}$";
	private static final String MENSAGEM_SENHA = "A senha deve ter de 8 a 72 caracteres, com letras, números e ao menos um caractere especial.";

	@Builder
	public record Request(
			@NotBlank(message = "Informe o nome do usuário.")
			@Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
			String nome,

			@NotBlank(message = "Informe o e-mail do usuário.")
			@Email(message = "Formato de e-mail inválido.")
			@Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
			String email,

			@NotBlank(message = "Informe a senha.")
			@Pattern(regexp = REGRA_SENHA, message = MENSAGEM_SENHA)
			String senha,

			@Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
			String telefone,

			@JsonProperty("empresa_id")
			UUID empresaId
	) {}

	@Builder
	public record AtualizacaoRequest(
			@NotBlank(message = "Informe o nome do usuário.")
			@Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
			String nome,

			@NotBlank(message = "Informe o e-mail do usuário.")
			@Email(message = "Formato de e-mail inválido.")
			@Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
			String email,

			@Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
			String telefone,

			@Pattern(regexp = REGRA_SENHA, message = MENSAGEM_SENHA)
			String senha
	) {}

	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record Response(
			UUID id,
			String nome,
			String email,
			String telefone,
			Perfil perfil,
			boolean ativo,

			@JsonProperty("empresa_id")
			UUID empresaId,

			@JsonProperty("empresa_nome")
			String empresaNome,

			@JsonProperty("ultimo_acesso")
			@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
			LocalDateTime ultimoAcesso,

			@JsonProperty("criado_em")
			@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
			LocalDateTime criadoEm
	) {}
}