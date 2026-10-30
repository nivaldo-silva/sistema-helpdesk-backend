package io.github.nivaldosilva.helpdesk.modules.usuario;

import io.github.nivaldosilva.helpdesk.modules.empresa.Empresa;
import io.github.nivaldosilva.helpdesk.modules.usuario.enums.Perfil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuarioMapper {

	public String normalizarEmail(String email) {
		return email == null ? null : email.trim().toLowerCase();
	}

	public Usuario toEntity(UsuarioDTO.Request request, String senhaCodificada, Empresa empresa, Perfil perfil) {
		return Usuario.builder()
				.nome(request.nome().trim())
				.email(normalizarEmail(request.email()))
				.senha(senhaCodificada)
				.telefone(request.telefone())
				.perfil(perfil)
				.empresa(empresa)
				.build();
	}

	public UsuarioDTO.Response toResponse(Usuario usuario) {
		Empresa empresa = usuario.getEmpresa();
		return UsuarioDTO.Response.builder()
				.id(usuario.getId())
				.nome(usuario.getNome())
				.email(usuario.getEmail())
				.telefone(usuario.getTelefone())
				.perfil(usuario.getPerfil())
				.ativo(usuario.isAtivo())
				.empresaId(empresa != null ? empresa.getId() : null)
				.empresaNome(empresa != null ? empresa.getNome() : null)
				.ultimoAcesso(usuario.getUltimoAcesso())
				.criadoEm(usuario.getCriadoEm())
				.build();
	}
}