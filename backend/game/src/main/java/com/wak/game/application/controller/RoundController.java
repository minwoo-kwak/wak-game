package com.wak.game.application.controller;

import com.wak.game.application.request.GameStartRequest;
import com.wak.game.application.facade.PlayerFacade;
import com.wak.game.application.facade.RoundFacade;
import com.wak.game.application.response.DashBoardResponse;
import com.wak.game.application.response.GameStartResponse;
import com.wak.game.application.response.PlayerInfoResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoundController {
    private final RoundFacade roundFacade;
    private final PlayerFacade playerFacade;

    @Operation(
            summary = "게임 시작",
            description = "게임을 시작하는 API 입니다",
            responses = {
                    @ApiResponse(responseCode = "200", description = "게임 시작 성공", content = @Content(schema = @Schema(implementation = ApiResult.class)))
            },
            security = {@SecurityRequirement(name = "Access-Token")}
    )
    @ApiErrorExamples({ErrorInfo.USER_NOT_EXIST, ErrorInfo.ROOM_NOT_EXIST, ErrorInfo.ROOM_NOT_HOST, ErrorInfo.ROUND_NOT_EXIST, ErrorInfo.ROOM_ALREADY_STARTED})
    @PostMapping("/api/game/start/{room-id}")
    public ResponseEntity<ApiResult<GameStartResponse>> startGame(@RequestBody GameStartRequest gameStartRequest, @PathVariable("room-id") Long roomId, @AuthUser Long userId) {
        GameStartResponse gameStartResponse = roundFacade.startGame(gameStartRequest, roomId, userId);

        return ResponseEntity.ok(ApiUtils.success(gameStartResponse));
    }

    @Operation(
            summary = "게임 시작 시 publish 요청",
            description = "게임 시작 시 게임 필드에 대한 정보를 publish 요청하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "game field 구독 " +
                            "성공")
            },
            security = {@SecurityRequirement(name = "Access-Token")}
    )
    @MessageMapping("/topic/games/{round-id}/battle-field")
    public void getBattleField(@DestinationVariable("round-id") Long roundId) {
        playerFacade.getPlayersStatus(roundId);
    }

    @Operation(
            summary = "게임 시작 시 publish 요청",
            description = "게임 시작 시 대시보드 대한 정보를 publish 요청하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "dashboard 구독 성공")
            },
            security = {@SecurityRequirement(name = "Access-Token")}
    )
    @MessageMapping("/topic/games/{round-id}/dashboard")
    public void getDashBoard(@AuthUser Long userId, @DestinationVariable("round-id") Long roundId) {
        DashBoardResponse result = roundFacade.getDashBoard(roundId);
    }

    @Operation(
            summary = "도발 멘트 작성",
            description = "각 라운드 시작 전 도발 멘트 작성",
            responses = {
                    @ApiResponse(responseCode = "200", description = "멘션 성공")
            },
            security = {@SecurityRequirement(name = "Access-Token")}
    )
    @MessageMapping("/{room-id}/mention")
    public void mention(@RequestBody GameStartRequest gameStartRequest, @DestinationVariable("room-id") Long roundId) {
        // List<AlivePlayerResponse> aliveUsers = roundFacade.startGameAndReturnStatus(gameStartRequest, roomId);
        //messagingTemplate.convertAndSend("/topic/games/" + roomId + "mention", aliveUsers);
    }
}
