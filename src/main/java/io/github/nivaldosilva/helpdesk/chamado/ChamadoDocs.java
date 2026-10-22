package io.github.nivaldosilva.helpdesk.chamado;

import io.github.nivaldosilva.helpdesk.chamado.enums.Prioridade;
import io.github.nivaldosilva.helpdesk.chamado.enums.Status;
import io.github.nivaldosilva.helpdesk.chamado.spec.ChamadoSpec;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import java.util.UUID;

@Tag(name = "Chamados", description = "Abertura, acompanhamento e atendimento de chamados de suporte")
@SecurityRequirement(name = "bearerAuth")
public interface ChamadoDocs {

	@Operation(summary = "Abrir chamado",
			description = "Somente CLIENTE. O chamado é vinculado à empresa do solicitante, nasce ABERTO com prioridade MEDIA, "
					+ "e o protocolo e o prazo de resolução são gerados automaticamente.")
	@ApiResponse(responseCode = "201", description = "Chamado aberto")
	ResponseEntity<ChamadoDTO.Response> abrir(
			ChamadoDTO.Request request,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Buscar chamado por ID",
			description = "CLIENTE acessa apenas os próprios chamados. ADMIN e SUPORTE acessam os de todas as empresas. "
					+ "Chamado fora do seu alcance retorna 404.")
	ResponseEntity<ChamadoDTO.Response> buscarPorId(
			UUID id,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Listar chamados",
			description = "CLIENTE lista apenas os próprios chamados. ADMIN e SUPORTE listam os de todas as empresas. "
					+ "Aceita filtros e paginação.")
	ResponseEntity<Page<ChamadoDTO.Response>> listar(
			ChamadoSpec spec,
			@ParameterObject Pageable pageable,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Listar fila de chamados abertos",
			description = "ADMIN e SUPORTE. Chamados com status ABERTO, ainda sem responsável.")
	ResponseEntity<Page<ChamadoDTO.Response>> listarAbertos(
			@ParameterObject Pageable pageable,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Atualizar chamado",
			description = "CLIENTE edita apenas o próprio chamado, enquanto estiver ABERTO. "
					+ "ADMIN edita qualquer um e SUPORTE apenas os que assumiu. Chamados fechados ou cancelados não podem ser alterados.")
	ResponseEntity<ChamadoDTO.Response> atualizar(
			UUID id,
			ChamadoDTO.Request request,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Aceitar chamado para atendimento",
			description = "ADMIN e SUPORTE. Assume um chamado ABERTO: o usuário vira o responsável e o status passa para EM_ANDAMENTO.")
	ResponseEntity<ChamadoDTO.Response> aceitar(
			UUID id,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Alterar status do chamado",
			description = "ADMIN ou o SUPORTE responsável. Transições válidas: EM_ANDAMENTO para RESOLVIDO, RESOLVIDO para FECHADO, "
					+ "RESOLVIDO para EM_ANDAMENTO (reabrir) e cancelamento, que só o ADMIN pode fazer. "
					+ "Para iniciar o atendimento, use o endpoint de aceitar.")
	ResponseEntity<ChamadoDTO.Response> alterarStatus(
			UUID id,
			Status status,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Alterar prioridade do chamado",
			description = "ADMIN ou o SUPORTE responsável. O prazo de resolução é recalculado a partir da data de abertura.")
	ResponseEntity<ChamadoDTO.Response> alterarPrioridade(
			UUID id,
			Prioridade prioridade,
			@Parameter(hidden = true) UUID usuarioId);

	@Operation(summary = "Excluir chamado",
			description = "Somente ADMIN. Exclusão lógica, permitida apenas para chamados ABERTO ou CANCELADO.")
	@ApiResponse(responseCode = "204", description = "Chamado excluído")
	ResponseEntity<Void> excluir(
			UUID id,
			@Parameter(hidden = true) UUID usuarioId);
}