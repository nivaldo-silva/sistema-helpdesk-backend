package io.github.nivaldosilva.helpdesk.chamado.validation;

import io.github.nivaldosilva.helpdesk.chamado.Chamado;
import io.github.nivaldosilva.helpdesk.chamado.enums.Status;
import io.github.nivaldosilva.helpdesk.core.exception.AcessoNegadoAoRecursoException;
import io.github.nivaldosilva.helpdesk.core.exception.OperacaoNaoPermitidaException;
import io.github.nivaldosilva.helpdesk.usuario.Usuario;
import io.github.nivaldosilva.helpdesk.usuario.enums.Perfil;
import org.springframework.stereotype.Component;

@Component
public class ChamadoValidator {

	public void validarPodeAbrir(Usuario usuario) {
		if (usuario.getPerfil() != Perfil.CLIENTE) {
			throw new AcessoNegadoAoRecursoException("Apenas usuários clientes podem abrir chamados");
		}
		if (!usuario.getEmpresa().isAtivo()) {
			throw new OperacaoNaoPermitidaException("A empresa do usuário está inativa");
		}
	}

	public void validarEquipeDeSuporte(Usuario usuario) {
		if (!usuario.getPerfil().equipeDeSuporte()) {
			throw new AcessoNegadoAoRecursoException("Apenas a equipe de suporte pode realizar esta operação");
		}
	}

	public void validarAdministrador(Usuario usuario) {
		if (usuario.getPerfil() != Perfil.ADMIN) {
			throw new AcessoNegadoAoRecursoException("Apenas administradores podem realizar esta operação");
		}
	}

	public void validarPodeAceitar(Chamado chamado) {
		if (chamado.getStatus() != Status.ABERTO) {
			throw new OperacaoNaoPermitidaException("Só é possível aceitar chamados abertos, sem responsável");
		}
	}

	public void validarPodeEditar(Usuario usuario, Chamado chamado) {
		if (usuario.getPerfil() == Perfil.CLIENTE) {
			if (chamado.getStatus() != Status.ABERTO) {
				throw new OperacaoNaoPermitidaException("O chamado só pode ser editado antes de iniciar o atendimento");
			}
			return;
		}
		validarPodeGerenciar(usuario, chamado);
	}

	public void validarPodeGerenciar(Usuario usuario, Chamado chamado) {
		validarEquipeDeSuporte(usuario);

		boolean administrador = usuario.getPerfil() == Perfil.ADMIN;
		boolean responsavel = chamado.getResponsavel() != null
				&& chamado.getResponsavel().getId().equals(usuario.getId());

		if (!administrador && !responsavel) {
			throw new AcessoNegadoAoRecursoException(
					"Somente o responsável pelo chamado ou um administrador pode realizar esta operação");
		}
		if (chamado.getStatus() == Status.FECHADO || chamado.getStatus() == Status.CANCELADO) {
			throw new OperacaoNaoPermitidaException("Chamados fechados ou cancelados não podem ser alterados");
		}
	}

	public void validarAlteracaoDeStatus(Usuario usuario, Chamado chamado, Status novoStatus) {
		validarPodeGerenciar(usuario, chamado);

		if (novoStatus == Status.CANCELADO) {
			validarAdministrador(usuario);
		}
		validarTransicao(chamado.getStatus(), novoStatus);

		if (novoStatus == Status.EM_ANDAMENTO && chamado.getResponsavel() == null) {
			throw new OperacaoNaoPermitidaException("Para iniciar o atendimento, o chamado precisa ser aceito por um atendente");
		}
	}

	public void validarPodeExcluir(Chamado chamado) {
		if (chamado.getStatus() != Status.ABERTO && chamado.getStatus() != Status.CANCELADO) {
			throw new OperacaoNaoPermitidaException("Só é possível excluir chamados abertos ou cancelados");
		}
	}

	private void validarTransicao(Status atual, Status novo) {
		boolean permitida = switch (atual) {
			case ABERTO -> novo == Status.EM_ANDAMENTO || novo == Status.CANCELADO;
			case EM_ANDAMENTO -> novo == Status.RESOLVIDO || novo == Status.CANCELADO;
			case RESOLVIDO -> novo == Status.FECHADO || novo == Status.EM_ANDAMENTO;
			case FECHADO, CANCELADO -> false;
		};

		if (!permitida) {
			throw new OperacaoNaoPermitidaException(
					"Não é possível mudar o chamado de " + atual.getDescricao() + " para " + novo.getDescricao());
		}
	}
}