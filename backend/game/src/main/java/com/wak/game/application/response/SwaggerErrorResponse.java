package com.wak.game.application.response;

import com.wak.game.global.error.ErrorInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SwaggerErrorResponse {
	private final String status;
	private final String message;

	public static SwaggerErrorResponse of(ErrorInfo errorInfo) {
		return new SwaggerErrorResponse(errorInfo.getHttpStatus().toString(), errorInfo.getMessage());
	}
}
