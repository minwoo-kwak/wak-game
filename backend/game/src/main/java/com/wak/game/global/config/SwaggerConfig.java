package com.wak.game.global.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		Server localServer = new Server();
		localServer.setUrl("http://localhost:8081");
		localServer.description("local");

		Server releaseServer = new Server();
		releaseServer.setUrl("https://wakgame.com/swagger-ui/");
		releaseServer.description("release");

		return new OpenAPI()
			.servers(List.of(localServer, releaseServer))
			.components(new Components().addSecuritySchemes("Access-Token", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
			.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
			.title("WAK GAME")
			.description("WAK GAME API Documentation")
			.version("1.0.0");
	}
}
