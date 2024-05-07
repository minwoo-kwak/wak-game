package com.wak.game.application.controller;

import com.wak.game.application.facade.UserFacade;
import com.wak.game.application.request.UserLogInRequest;
import com.wak.game.application.response.UserInfoResponse;
import com.wak.game.application.response.UserLogInResponse;
import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.token.AuthUser;
import com.wak.game.global.util.ApiErrorExamples;
import com.wak.game.global.util.ApiResult;
import com.wak.game.global.util.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @Operation(
            summary = "User 생성",
            description = "User 닉네임을 처음 생성하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResult.class)))	 // Successful Response
            }
    )
    @ApiErrorExamples({ErrorInfo.COLOR_NOT_EXIST, ErrorInfo.USER_ALREADY_EXIST})
    @PostMapping("")
    public ResponseEntity<ApiResult<UserLogInResponse>> login(@RequestBody UserLogInRequest request) {
        UserLogInResponse response = userFacade.login(request);
        return ResponseEntity.ok(ApiUtils.success(response));
    }

    @Operation(
            summary = "User info 조회",
            description = "User의 info 를 조회하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = ApiResult.class)))	 // Successful Response
            },
            security = { @SecurityRequirement(name = "Access-Token") } // Token을 요구하는 API에 필수 기입
    )
    @ApiErrorExamples({ErrorInfo.COLOR_NOT_EXIST, ErrorInfo.USER_NOT_EXIST})
    @GetMapping("/info")
    public ResponseEntity<ApiResult<UserInfoResponse>> getUserInfo(@AuthUser Long id){
        UserInfoResponse response = userFacade.userInfo(id);
        return ResponseEntity.ok(ApiUtils.success(response));
    }

}
