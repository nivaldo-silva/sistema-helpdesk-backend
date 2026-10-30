package io.github.nivaldosilva.helpdesk.modules.chamado.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Setor {

	TI(
			"Tecnologia da Informação",
			"Setor responsável pela infraestrutura e sistemas"
	),

	FINANCEIRO(
			"Setor Financeiro",
			"Gestão de contas, faturamento e pagamentos"
	),

	RH(
			"Recursos Humanos",
			"Gestão de pessoas, departamento pessoal e benefícios"
	),

	COMERCIAL(
			"Setor Comercial",
			"Vendas e relacionamento com clientes"
	),

	OPERACIONAL(
			"Setor Operacional",
			"Operações e suporte de campo"
	),

	ADMINISTRATIVO(
			"Setor Administrativo",
			"Administração geral e suporte interno"
	);

	private final String descricao;
	private final String detalhes;
}