package io.github.nivaldosilva.helpdesk.infra.security;

import io.github.nivaldosilva.helpdesk.modules.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenValidator implements OAuth2TokenValidator<Jwt> {

	private static final OAuth2Error ERRO =
			new OAuth2Error(
					"usuario_inativo",
					"Usuário inativo ou não encontrado.",
					null
			);

	private final UsuarioRepository usuarioRepository;

	@Override
	public OAuth2TokenValidatorResult validate(Jwt token) {

		return usuarioAtivo(token.getSubject())
				? OAuth2TokenValidatorResult.success()
				: OAuth2TokenValidatorResult.failure(ERRO);
	}

	private boolean usuarioAtivo(String subject) {

		if (subject == null || subject.isBlank()) {
			return false;
		}

		try {
			UUID usuarioId = UUID.fromString(subject);
			return usuarioRepository.existsByIdAndAtivoTrue(usuarioId);

		} catch (IllegalArgumentException ex) {
			return false;
		}
	}
}