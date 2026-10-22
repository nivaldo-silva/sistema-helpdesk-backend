package io.github.nivaldosilva.helpdesk.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

	private static final String BEARER_AUTH = "bearerAuth";

	@Bean
	public OpenAPI helpdeskOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Help Desk API")
						.version("1.0.0")
						.description("API de help desk B2B para gerenciamento de chamados, "
								+ "comentários, usuários e empresas. "
								+ "Faça login em /auth/login e informe o token em Authorize."))
				.components(new Components()
						.addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.bearerFormat("JWT")))
				.addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
	}
}