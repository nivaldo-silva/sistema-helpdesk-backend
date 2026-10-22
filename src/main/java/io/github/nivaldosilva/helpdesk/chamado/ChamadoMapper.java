package io.github.nivaldosilva.helpdesk.chamado;

import io.github.nivaldosilva.helpdesk.empresa.Empresa;
import io.github.nivaldosilva.helpdesk.usuario.Usuario;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ChamadoMapper {

	public Chamado toEntity(ChamadoDTO.Request request, Empresa empresa, Usuario solicitante) {
		return Chamado.builder()
				.titulo(request.titulo())
				.descricao(request.descricao())
				.categoria(request.categoria())
				.setor(request.setor())
				.empresa(empresa)
				.solicitante(solicitante)
				.build();
	}

	public ChamadoDTO.Response toResponse(Chamado chamado) {
		Empresa empresa = chamado.getEmpresa();
		Usuario solicitante = chamado.getSolicitante();
		Usuario responsavel = chamado.getResponsavel();

		return ChamadoDTO.Response.builder()
				.id(chamado.getId())
				.protocolo(chamado.getProtocolo())
				.titulo(chamado.getTitulo())
				.descricao(chamado.getDescricao())
				.categoria(chamado.getCategoria())
				.setor(chamado.getSetor())
				.status(chamado.getStatus())
				.prioridade(chamado.getPrioridade())
				.canalOrigem(chamado.getCanalOrigem())
				.empresaId(empresa.getId())
				.empresaNome(empresa.getNome())
				.solicitanteId(solicitante.getId())
				.solicitanteNome(solicitante.getNome())
				.responsavelId(responsavel != null ? responsavel.getId() : null)
				.responsavelNome(responsavel != null ? responsavel.getNome() : null)
				.prazoResolucao(chamado.getPrazoResolucao())
				.dataResolucao(chamado.getDataResolucao())
				.dataFechamento(chamado.getDataFechamento())
				.criadoEm(chamado.getCriadoEm())
				.build();
	}
}