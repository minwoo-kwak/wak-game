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
    private final SimpMessagingTemplate messagingTemplate;
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

    /**
     * 구독한 모든 유저에게 체력이 1이상인 유저들의 정보 리턴
     *
     * @param userId
     * @param roundId
     */
    @MessageMapping("/topic/games/{roundId}/battle-field")
    public void getBattleField(@AuthUser Long userId, @DestinationVariable Long roundId) {
        List<PlayerInfoResponse> usersInfo = playerFacade.getPlayersStatus(roundId);
        messagingTemplate.convertAndSend("/topic/games/" + roundId + "/battle-field", usersInfo);
    }

    @MessageMapping("/topic/games/{roundId}/dashboard")
    public void getDashBoard(@AuthUser Long userId, @DestinationVariable Long roundId) {
        DashBoardResponse result = roundFacade.getDashBoard(roundId, userId);
        messagingTemplate.convertAndSend("/topic/games/" + roundId + "dashboard", result);
    }

    @MessageMapping("/{roomId}/kill-log")
    public void getKillLog(@RequestBody GameStartRequest gameStartRequest, @DestinationVariable Long roomId) {

        //messagingTemplate.convertAndSend("/topic/games/" + roomId + "kill-log", aliveUsers);
    }

    @MessageMapping("/{roomId}/mention")
    public void mention(@RequestBody GameStartRequest gameStartRequest, @DestinationVariable Long roomId) {
        // List<AlivePlayerResponse> aliveUsers = roundFacade.startGameAndReturnStatus(gameStartRequest, roomId);
        //messagingTemplate.convertAndSend("/topic/games/" + roomId + "mention", aliveUsers);
    }


}
