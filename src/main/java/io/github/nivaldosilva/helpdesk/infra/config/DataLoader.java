package io.github.nivaldosilva.helpdesk.infra.config;

import io.github.nivaldosilva.helpdesk.modules.usuario.Usuario;
import io.github.nivaldosilva.helpdesk.modules.usuario.UsuarioRepository;
import io.github.nivaldosilva.helpdesk.modules.usuario.enums.Perfil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@Profile("!teste")
@RequiredArgsConstructor
public class DataLoader {

	private static final String EMAIL_ADMIN = "admin@helpdesk.com";
	private static final String SENHA_ADMIN = "Admin@1234";
	private static final int TAMANHO_MINIMO_SENHA = 8;

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

	@Bean
	CommandLineRunner carregarAdministradorPadrao() {
		return args -> {
			if (usuarioRepository.existsByEmailIgnoreCase(EMAIL_ADMIN)) {
				return;
			}

			if (SENHA_ADMIN.isBlank()) {
				log.warn("Administrador padrão não criado: informe uma senha válida.");
				return;
			}

			if (SENHA_ADMIN.length() < TAMANHO_MINIMO_SENHA) {
				log.warn("Administrador padrão não criado: a senha deve ter no mínimo {} caracteres.", TAMANHO_MINIMO_SENHA);
				return;
			}

			usuarioRepository.save(Usuario.builder()
					.nome("Administrador do Sistema")
					.email(EMAIL_ADMIN.toLowerCase())
					.senha(passwordEncoder.encode(SENHA_ADMIN))
					.telefone("81999999999")
					.perfil(Perfil.ADMIN)
					.ativo(true)
					.build());

			log.info("Administrador padrão criado. E-mail: {}", EMAIL_ADMIN);
		};
	}
}