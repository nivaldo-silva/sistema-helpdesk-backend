package io.github.nivaldosilva.helpdesk.modules.chamado;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.nivaldosilva.helpdesk.modules.chamado.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

public final class ChamadoDTO {

	private ChamadoDTO() {}

	@Builder
	public record Request(
			@NotBlank(message = "O título do chamado é obrigatório.")
			@Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
			String titulo,

			@NotBlank(message = "A descrição do chamado é obrigatória.")
			String descricao,

			@NotNull(message = "A categoria do chamado é obrigatória.") Categoria categoria,

			@NotNull(message = "O setor do chamado é obrigatório.") Setor setor
	) {}

	@Builder
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record Response(
			UUID id,
			String protocolo,
			String titulo,
			String descricao,
			Categoria categoria,
			Setor setor,
			Status status,
			Prioridade prioridade,

			@JsonProperty("canal_origem") CanalOrigem canalOrigem,

			@JsonProperty("empresa_id")
			UUID empresaId,

			@JsonProperty("empresa_nome")
			String empresaNome,

			@JsonProperty("solicitante_id")
			UUID solicitanteId,

			@JsonProperty("solicitante_nome")
			String solicitanteNome,

			@JsonProperty("responsavel_id")
			UUID responsavelId,

			@JsonProperty("responsavel_nome")
			String responsavelNome,

			@JsonProperty("prazo_resolucao")
			@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
			LocalDateTime prazoResolucao,

			@JsonProperty("data_resolucao")
			@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
			LocalDateTime dataResolucao,

			@JsonProperty("data_fechamento")
			@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
			LocalDateTime dataFechamento,

			@JsonProperty("criado_em")
			@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
			LocalDateTime criadoEm
	) {}
}