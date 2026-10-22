package io.github.nivaldosilva.helpdesk.chamado.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CanalOrigem {

	PORTAL_WEB(
			"Portal Web",
			"Solicitação aberta diretamente através do painel web do cliente"
	),

	TELEFONE(
			"Telefone",
			"Chamado registrado pela central de atendimento telefônico (Help Desk / Call Center)"
	),

	PRESENCIAL(
			"Presencial",
			"Suporte prestado diretamente in loco nas dependências da empresa"
	);

	private final String descricao;
	private final String detalhes;
}