package io.github.nivaldosilva.helpdesk.modules.comentario;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

public final class ComentarioDTO {

	private ComentarioDTO() {}

	@Builder
	public record Request(
			@NotBlank(message = "O texto do comentário é obrigatório.")
			@Size(max = 5000, message = "O comentário deve ter no máximo 5000 caracteres.")
			String mensagem,

			@NotNull(message = "Informe se o comentário é uma nota interna.")
			@JsonProperty("nota_interna")
			Boolean notaInterna
	) {}

	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record Response(
			UUID id,
			String mensagem,
			@JsonProperty("nota_interna")
			boolean notaInterna,

			@JsonProperty("autor_id")
			UUID autorId,

			@JsonProperty("autor_nome")
			String autorNome,

			@JsonProperty("autor_perfil")
			String autorPerfil,

			@JsonProperty("criado_em")
			@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
			LocalDateTime criadoEm
	) {}
}