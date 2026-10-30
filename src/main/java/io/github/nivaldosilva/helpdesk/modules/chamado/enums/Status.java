package io.github.nivaldosilva.helpdesk.modules.chamado.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

	ABERTO(
			"Aberto",
			"Chamado registrado e aguardando início do atendimento"
	),

	EM_ANDAMENTO(
			"Em andamento",
			"Chamado assumido por um técnico e em processo de atendimento"
	),

	RESOLVIDO(
			"Resolvido",
			"Problema solucionado pelo técnico e aguardando confirmação ou encerramento"
	),

	FECHADO(
			"Fechado",
			"Chamado encerrado após a conclusão do atendimento"
	),

	CANCELADO(
			"Cancelado",
			"Chamado cancelado pelo administrador antes da conclusão do atendimento"
	);

	private final String descricao;
	private final String detalhes;
}