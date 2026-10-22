package io.github.nivaldosilva.helpdesk.chamado.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Categoria {

	SUPORTE_TECNICO(
			"Suporte Técnico",
			"Problemas relacionados a hardware, software ou funcionamento de equipamentos"
	),

	MANUTENCAO(
			"Manutenção",
			"Manutenção preventiva ou corretiva de equipamentos e recursos de tecnologia"
	),

	SOFTWARE(
			"Software",
			"Erros, falhas ou dúvidas relacionadas a sistemas e aplicações"
	),

	REDES(
			"Redes",
			"Problemas de conectividade, VPN, internet e infraestrutura de rede"
	),

	ACESSO_CONTA(
			"Acesso e Conta",
			"Login, senha, permissões e bloqueio de contas"
	),

	OUTROS(
			"Outros",
			"Demais solicitações que não se enquadram nas categorias disponíveis"
	);

	private final String descricao;
	private final String detalhes;
}