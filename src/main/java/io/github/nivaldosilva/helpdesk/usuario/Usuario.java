package io.github.nivaldosilva.helpdesk.usuario;

import io.github.nivaldosilva.helpdesk.empresa.Empresa;
import io.github.nivaldosilva.helpdesk.usuario.enums.Perfil;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Usuario implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, length = 150)
	private String nome;

	@Column(nullable = false, unique = true, length = 150)
	private String email;

	@Column(nullable = false, length = 255)
	private String senha;

	@Column(length = 20)
	private String telefone;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Perfil perfil;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empresa_id")
	private Empresa empresa;

	@Builder.Default
	@Column(nullable = false)
	private boolean ativo = true;

	@Column(name = "ultimo_acesso")
	private LocalDateTime ultimoAcesso;

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

	public void atualizarSenha(String senhaCodificada) {
		this.senha = senhaCodificada;
	}

	public void registrarAcesso() {
		this.ultimoAcesso = LocalDateTime.now();
	}

	public void desativar() {
		this.ativo = false;
	}

	public void reativar() {
		this.ativo = true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isEnabled() {
		return ativo;
	}
}