package io.github.nivaldosilva.helpdesk.infrastructure.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import io.github.nivaldosilva.helpdesk.infrastructure.security.TokenValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class JwtKeyConfig {

	private final RSAPublicKey publicKey;
	private final RSAPrivateKey privateKey;

	public JwtKeyConfig(
			@Value("${jwt.public.key}") Resource publicKeyResource,
			@Value("${jwt.private.key}") Resource privateKeyResource) throws IOException {

		try (InputStream publicIn = publicKeyResource.getInputStream();
			 InputStream privateIn = privateKeyResource.getInputStream()) {
			this.publicKey = RsaKeyConverters.x509().convert(publicIn);
			this.privateKey = RsaKeyConverters.pkcs8().convert(privateIn);
		}
	}

	@Bean
	public JwtDecoder jwtDecoder(TokenValidator tokenValidator, @Value("${jwt.issuer:helpdesk}") String emissor) {
		NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(this.publicKey).build();
		OAuth2TokenValidator<Jwt> validador = new DelegatingOAuth2TokenValidator<>(
				JwtValidators.createDefaultWithIssuer(emissor), tokenValidator);

		decoder.setJwtValidator(validador);
		return decoder;
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		var jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
		return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
	}
}