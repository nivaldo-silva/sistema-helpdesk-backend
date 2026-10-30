package io.github.nivaldosilva.helpdesk.modules.usuario.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Perfil {

	ADMIN(
			"Administrador",
			"Gerencia empresas, usuários, chamados e configurações do sistema"
	),

	SUPORTE(
			"Suporte",
			"Equipe de atendimento (técnicos, atendentes e supervisores): atende chamados, altera o status e registra comentários"
	),

	CLIENTE(
			"Cliente",
			"Representante da empresa-cliente: abre chamados e acompanha apenas os próprios chamados"
	);

	private final String descricao;
	private final String detalhes;

	public boolean equipeDeSuporte() {
		return this == ADMIN || this == SUPORTE;
	}
}