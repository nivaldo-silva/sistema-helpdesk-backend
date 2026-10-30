package io.github.nivaldosilva.helpdesk.modules.comentario;

import io.github.nivaldosilva.helpdesk.modules.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLRestriction;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comentarios")
@SQLRestriction("ativo = true")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Comentario {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "chamado_id", nullable = false, updatable = false)
	private UUID chamadoId;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String mensagem;

	@Builder.Default
	@Column(name = "nota_interna", nullable = false)
	private boolean notaInterna = false;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "autor_id", nullable = false)
	private Usuario autor;

	@CreationTimestamp
	@Column(name = "criado_em", nullable = false, updatable = false)
	private LocalDateTime criadoEm;

	@Version
	private Long versao;

	public void atualizarDados(String mensagem, boolean notaInterna) {
		this.mensagem = mensagem;
		this.notaInterna = notaInterna;
	}
}