package com.wak.game.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wak.game.domain.sample.SampleResponse;
import com.wak.game.global.util.ApiResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Sample", description = "Sample 관련 API")
@RestController
@RequestMapping("/api/sample")
public class SampleController {

	@Operation(
		summary = "Sample 조회",
		description = "Sample 조회하는 API 입니다",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResult.class))), // Successful Response
			@ApiResponse(responseCode = "404", description = "조회 실패(에러 메시지 설명)", content = @Content(schema = @Schema(implementation = ApiResult.class))) // Error Response
		},
		security = { @SecurityRequirement(name = "Access-Token") } // Token을 요구하는 API에 필수 기입
	)
	@GetMapping
	public SampleResponse sample(String sampleName) {
		return SampleResponse.builder().sampleName(sampleName).build();
	}

}

