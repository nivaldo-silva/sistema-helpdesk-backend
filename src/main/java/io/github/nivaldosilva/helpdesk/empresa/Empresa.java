package io.github.nivaldosilva.helpdesk.empresa;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "empresas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Empresa {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, length = 150)
	private String nome;

	@Column(nullable = false, unique = true, length = 14)
	private String cnpj;

	@Column(name = "email_corporativo", nullable = false, length = 150)
	private String email;

	@Column(length = 20)
	private String telefone;

	@Builder.Default
	@Column(nullable = false)
	private boolean ativo = true;

	@CreationTimestamp
	@Column(name = "criado_em", nullable = false, updatable = false)
	private LocalDateTime criadoEm;

	@Version
	private Long versao;

	public void atualizarDados(String nome, String email, String telefone) {
		this.nome = nome;
		this.email = email;
		this.telefone = telefone;
	}

	public void desativar() {
		this.ativo = false;
	}

	public void reativar() {
		this.ativo = true;
	}
}