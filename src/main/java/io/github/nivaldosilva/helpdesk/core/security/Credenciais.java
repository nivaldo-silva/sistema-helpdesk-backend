package io.github.nivaldosilva.helpdesk.core.security;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import java.time.ZonedDateTime;

public class Credenciais {

	@Builder
	public record Request(
			@NotBlank(message = "Informe o login")
			String login,

			@NotBlank(message = "Informe a senha")
			String senha
	) {}



	@Builder
	public record Response(
			String token,
			String tipo,
			@JsonProperty("expira_em")
			@JsonFormat(
					shape = JsonFormat.Shape.STRING,
					pattern = "dd/MM/yyyy HH:mm:ss",
					timezone = "America/Recife"
			)
			ZonedDateTime expiraEm
	) {}
}