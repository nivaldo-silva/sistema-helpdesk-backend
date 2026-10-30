package io.github.nivaldosilva.helpdesk.modules.comentario;

import io.github.nivaldosilva.helpdesk.modules.usuario.Usuario;
import lombok.experimental.UtilityClass;
import java.util.UUID;

@UtilityClass
public class ComentarioMapper {

	public Comentario toEntity(ComentarioDTO.Request request, UUID chamadoId, Usuario autor) {
		return Comentario.builder()
				.chamadoId(chamadoId)
				.autor(autor)
				.mensagem(request.mensagem())
				.notaInterna(request.notaInterna())
				.build();
	}

	public ComentarioDTO.Response toResponse(Comentario comentario) {
		Usuario autor = comentario.getAutor();

		return ComentarioDTO.Response.builder()
				.id(comentario.getId())
				.mensagem(comentario.getMensagem())
				.notaInterna(comentario.isNotaInterna())
				.autorId(autor.getId())
				.autorNome(autor.getNome())
				.autorPerfil(autor.getPerfil().name())
				.criadoEm(comentario.getCriadoEm())
				.build();
	}
}