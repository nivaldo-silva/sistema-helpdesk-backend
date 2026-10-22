package io.github.nivaldosilva.helpdesk.comentario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

@Tag(name = "Comentários", description = "Comentários trocados entre cliente e suporte no acompanhamento dos chamados")
@SecurityRequirement(name = "bearerAuth")
public interface ComentarioDocs {

	@Operation(summary = "Adicionar comentário ao chamado",
			description = "CLIENTE comenta apenas nos próprios chamados. ADMIN e SUPORTE comentam nos chamados de todas as empresas. "
					+ "Notas internas (`nota_interna`) são exclusivas da equipe de suporte. Chamado fora do seu alcance retorna 404.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Comentário adicionado"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "403", description = "Nota interna permitida apenas à equipe de suporte"),
			@ApiResponse(responseCode = "404", description = "Chamado não encontrado")
	})
	ResponseEntity<ComentarioDTO.Response> adicionar(
			@Parameter(description = "Identificador do chamado (UUID)", required = true) UUID chamadoId,
			ComentarioDTO.Request request,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Listar comentários do chamado",
			description = "CLIENTE lista apenas os comentários dos próprios chamados. ADMIN e SUPORTE listam os de todas as empresas, incluindo as notas internas, que o cliente não vê. "
					+ "Ordenados do mais antigo para o mais recente. Padrão: 20 por página. Chamado fora do seu alcance retorna 404.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Comentários listados"),
			@ApiResponse(responseCode = "404", description = "Chamado não encontrado")
	})
	ResponseEntity<Page<ComentarioDTO.Response>> listar(
			@Parameter(description = "Identificador do chamado (UUID)", required = true) UUID chamadoId,
			@ParameterObject Pageable pageable,
			@Parameter(hidden = true) UUID usuarioId);
}