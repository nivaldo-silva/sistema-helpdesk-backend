package io.github.nivaldosilva.helpdesk.modules.empresa;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EmpresaMapper {

	public String normalizarEmail(String email) {
		return email == null ? null : email.trim().toLowerCase();
	}

	public Empresa toEntity(EmpresaDTO.Request request) {
		return Empresa.builder()
				.nome(request.nome().trim())
				.cnpj(request.cnpj())
				.email(normalizarEmail(request.email()))
				.telefone(request.telefone())
				.build();
	}

	public EmpresaDTO.Response toResponse(Empresa empresa) {
		return EmpresaDTO.Response.builder()
				.id(empresa.getId())
				.nome(empresa.getNome())
				.cnpj(empresa.getCnpj())
				.email(empresa.getEmail())
				.telefone(empresa.getTelefone())
				.ativo(empresa.isAtivo())
				.criadoEm(empresa.getCriadoEm())
				.build();
	}
}