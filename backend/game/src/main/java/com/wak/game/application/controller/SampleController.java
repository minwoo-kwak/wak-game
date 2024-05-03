package com.wak.game.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wak.game.domain.sample.SampleResponse;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.util.ApiErrorExample;
import com.wak.game.global.util.ApiErrorExamples;
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
			@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResult.class)))	 // Successful Response
		},
		security = { @SecurityRequirement(name = "Access-Token") } // Token을 요구하는 API에 필수 기입
	)
	// Error Response는 아래 두 annotation 중 선택해서 사용
	@ApiErrorExample(ErrorInfo.SAMPLE_NOT_EXIST) // ErrorResponse 한 개
	@ApiErrorExamples({ErrorInfo.SAMPLE_NOT_EXIST, ErrorInfo.SAMPLE_ALREADY_EXIST}) // ErrorResponse 여러 개 -> 같은 에러 코드는 자동으로 그룹핑 됨
	@GetMapping
	public SampleResponse sample(String sampleName) {
		return SampleResponse.builder().sampleName(sampleName).build();
	}

}

