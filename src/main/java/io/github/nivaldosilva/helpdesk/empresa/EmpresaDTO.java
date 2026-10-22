package io.github.nivaldosilva.helpdesk.empresa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

public final class EmpresaDTO {

	private EmpresaDTO() {}

	@Builder
	public record Request(
			@NotBlank(message = "Informe o nome da empresa.")
			@Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
			String nome,

			@NotBlank(message = "Informe o CNPJ da empresa.")
			@Pattern(regexp = "^[A-Z0-9]{12}\\d{2}$",
					message = "O CNPJ deve ter 14 caracteres, sem pontuação: 12 letras maiúsculas ou números seguidos de 2 dígitos.")
			String cnpj,

			@NotBlank(message = "Informe o e-mail corporativo.")
			@Email(message = "E-mail corporativo inválido.")
			@Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
			@JsonProperty("email_corporativo")
			String email,

			@Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
			String telefone
	) {}

	@Builder
	public record AtualizacaoRequest(
			@NotBlank(message = "Informe o nome da empresa.")
			@Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
			String nome,

			@NotBlank(message = "Informe o e-mail corporativo.")
			@Email(message = "E-mail corporativo inválido.")
			@Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
			@JsonProperty("email_corporativo")
			String email,

			@Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
			String telefone
	) {}

	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record Response(
			UUID id,
			String nome,
			String cnpj,

			@JsonProperty("email_corporativo")
			String email,

			String telefone,
			boolean ativo,

			@JsonProperty("criado_em")
			@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
			LocalDateTime criadoEm
	) {}
}