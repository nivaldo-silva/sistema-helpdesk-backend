package io.github.nivaldosilva.helpdesk.chamado.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.Duration;

@Getter
@AllArgsConstructor
public enum Prioridade {

	BAIXA(
			"Baixa",
			"Solicitações de baixo impacto que não comprometem significativamente as atividades",
			Duration.ofHours(8),
			Duration.ofHours(72)
	),

	MEDIA(
			"Média",
			"Solicitações que afetam parcialmente as atividades, mas permitem a continuidade do trabalho",
			Duration.ofHours(4),
			Duration.ofHours(24)
	),

	ALTA(
			"Alta",
			"Problemas que comprometem atividades importantes e exigem atendimento prioritário",
			Duration.ofHours(1),
			Duration.ofHours(8)
	),

	URGENTE(
			"Urgente",
			"Problemas críticos que impedem atividades essenciais e exigem atendimento imediato",
			Duration.ofMinutes(30),
			Duration.ofHours(2)
	);

	private final String descricao;
	private final String detalhes;
	private final Duration prazoPrimeiraResposta;
	private final Duration prazoResolucao;
}