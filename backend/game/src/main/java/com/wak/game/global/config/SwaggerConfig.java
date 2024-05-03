package com.wak.game.global.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.util.ApiErrorExample;
import com.wak.game.global.util.ApiErrorExamples;
import com.wak.game.global.util.ExampleHolder;
import com.wak.game.application.response.SwaggerErrorResponse;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
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

	@Bean
	public OperationCustomizer customizer() {
		return (Operation operation, HandlerMethod handlerMethod) -> {
			ApiErrorExamples apiErrorExamples = handlerMethod.getMethodAnnotation(ApiErrorExamples.class);

			if (apiErrorExamples != null) {
				generateErrorResponseExample(operation, apiErrorExamples.value());
			} else {
				ApiErrorExample apiErrorExample = handlerMethod.getMethodAnnotation(ApiErrorExample.class);

				if (apiErrorExample != null) {
					generateErrorResponseExample(operation, apiErrorExample.value());
				}
			}

			return operation;
		};
	}

	private void generateErrorResponseExample(Operation operation, ErrorInfo[] errorInfos) {
		ApiResponses responses = operation.getResponses();

		Map<Integer, List<ExampleHolder>> exampleHolders = Arrays.stream(errorInfos)
			.map(
				errorInfo -> ExampleHolder.builder()
					.example(getSwaggerExample(errorInfo))
					.code(errorInfo.getHttpStatus().value())
					.name(errorInfo.name())
					.build()
			)
			.collect(Collectors.groupingBy(ExampleHolder::getCode));

		addExamplesToResponses(responses, exampleHolders);
	}

	private void generateErrorResponseExample(Operation operation, ErrorInfo errorInfo) {
		ApiResponses responses = operation.getResponses();

		ExampleHolder exampleHolder = ExampleHolder.builder()
			.example(getSwaggerExample(errorInfo))
			.code(errorInfo.getHttpStatus().value())
			.name(errorInfo.name())
			.build();

		addExamplesToResponses(responses, exampleHolder);
	}

	private Example getSwaggerExample(ErrorInfo errorInfo) {
		SwaggerErrorResponse swaggerErrorResponse = SwaggerErrorResponse.of(errorInfo);
		Example example = new Example();
		example.setValue(swaggerErrorResponse);
		return example;
	}

	private void addExamplesToResponses(ApiResponses responses, Map<Integer, List<ExampleHolder>> exampleHolders) {
		exampleHolders.forEach(
			(status, value) -> {
				Content content = new Content();
				MediaType mediaType = new MediaType();
				ApiResponse apiResponse = new ApiResponse();

				value.forEach(
					exampleHolder -> mediaType.addExamples(
						exampleHolder.getName(),
						exampleHolder.getExample()
					)
				);

				content.addMediaType("application/json", mediaType);
				apiResponse.setContent(content);
				responses.addApiResponse(String.valueOf(status), apiResponse);
			}
		);
	}

	private void addExamplesToResponses(ApiResponses responses, ExampleHolder exampleHolder) {
		Content content = new Content();
		MediaType mediaType = new MediaType();
		ApiResponse apiResponse = new ApiResponse();

		mediaType.addExamples(exampleHolder.getName(), exampleHolder.getExample());
		content.addMediaType("application/json", mediaType);
		apiResponse.content(content);
		responses.addApiResponse(String.valueOf(exampleHolder.getCode()), apiResponse);
	}
}
