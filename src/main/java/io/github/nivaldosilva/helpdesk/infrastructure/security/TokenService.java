package io.github.nivaldosilva.helpdesk.infrastructure.security;

import io.github.nivaldosilva.helpdesk.modules.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

	private static final ZoneId FUSO = ZoneId.of("America/Recife");
	private final JwtEncoder jwtEncoder;

	@Value("${jwt.expiration-time:3600}")
	private long expiracao;

	@Value("${jwt.issuer:helpdesk}")
	private String emissor;

	public Credenciais.Response gerar(Usuario usuario) {
		Instant agora = Instant.now();
		Instant expiraEm = agora.plusSeconds(expiracao);

		String escopos = usuario.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer(emissor)
				.issuedAt(agora)
				.expiresAt(expiraEm)
				.subject(usuario.getId().toString())
				.id(UUID.randomUUID().toString())
				.claim("scope", escopos)
				.build();

		String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		log.info("Token JWT gerado para o usuário ID: {}", usuario.getId());

		return Credenciais.Response.builder()
				.token(token)
				.tipo("Bearer")
				.expiraEm(expiraEm.atZone(FUSO))
				.build();
	}
}