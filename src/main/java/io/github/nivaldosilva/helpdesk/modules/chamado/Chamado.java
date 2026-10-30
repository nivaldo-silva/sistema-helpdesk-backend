package io.github.nivaldosilva.helpdesk.modules.chamado;

import io.github.nivaldosilva.helpdesk.modules.chamado.enums.*;
import io.github.nivaldosilva.helpdesk.modules.empresa.Empresa;
import io.github.nivaldosilva.helpdesk.modules.chamado.enums.*;
import io.github.nivaldosilva.helpdesk.modules.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "chamados")
@SQLRestriction("ativo = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Chamado {

	private static final SecureRandom GERADOR_DE_PROTOCOLO = new SecureRandom();

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, unique = true, updatable = false, length = 20)
	private String protocolo;

	@Column(nullable = false, length = 150)
	private String titulo;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String descricao;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private Categoria categoria;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private Status status = Status.ABERTO;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Prioridade prioridade = Prioridade.MEDIA;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(name = "canal_origem", nullable = false, length = 20)
	private CanalOrigem canalOrigem = CanalOrigem.PORTAL_WEB;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "empresa_id", nullable = false)
	private Empresa empresa;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private Setor setor;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "solicitante_id", nullable = false)
	private Usuario solicitante;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "responsavel_id")
	private Usuario responsavel;

	@Column(name = "prazo_resolucao", nullable = false)
	private LocalDateTime prazoResolucao;

	@Column(name = "data_resolucao")
	private LocalDateTime dataResolucao;

	@Column(name = "data_fechamento")
	private LocalDateTime dataFechamento;

	@Builder.Default
	@Column(name = "ativo", nullable = false)
	private boolean ativo = true;

	@CreationTimestamp
	@Column(name = "criado_em", updatable = false)
	private LocalDateTime criadoEm;

	@Version
	private Long versao;

	@PrePersist
	private void prepararParaPersistir() {
		this.protocolo = gerarProtocoloNumerico();
		this.prazoResolucao = LocalDateTime.now().plus(prioridade.getPrazoResolucao());
	}

	private String gerarProtocoloNumerico() {
		return LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
				+ String.format("%06d", GERADOR_DE_PROTOCOLO.nextInt(1_000_000));
	}

	public void atualizarDados(String titulo, String descricao, Categoria categoria, Setor setor) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.categoria = categoria;
		this.setor = setor;
	}

	public void alterarPrioridade(Prioridade novaPrioridade) {
		this.prioridade = novaPrioridade;
		this.prazoResolucao = criadoEm.plus(novaPrioridade.getPrazoResolucao());
	}

	public void atribuirResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
		this.status = Status.EM_ANDAMENTO;
	}

	public void resolver(LocalDateTime dataHora) {
		this.dataResolucao = dataHora;
		this.status = Status.RESOLVIDO;
	}
	public void reabrir() {
		this.dataResolucao = null;
		this.status = Status.EM_ANDAMENTO;
	}

	public void cancelar(LocalDateTime dataHora) {
		this.dataFechamento = dataHora;
		this.status = Status.CANCELADO;
	}

	public void desativar() {
		this.ativo = false;
	}

	public void fechar(LocalDateTime dataHora) {
		this.dataFechamento = dataHora;
		this.status = Status.FECHADO;
	}
}
