package com.wak.game.application.controller;

import com.wak.game.application.facade.PlayerFacade;
import com.wak.game.application.facade.RoundFacade;
import com.wak.game.application.request.ClickRequest;
import com.wak.game.application.response.DashBoardResponse;
import com.wak.game.application.vo.clickVO;
import com.wak.game.global.token.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/player")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerFacade playerFacade;

    @Operation(
            summary = "게임입장 시 클릭 publish 요청",
            description = "게임 입장 시 킬 로그에 대한 정보를 publish 요청하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "반환 성공")
            },
            security = {@SecurityRequirement(name = "Access-Token")}
    )
    @MessageMapping("/topic/games/{roundId}/click")
    public void click(@AuthUser Long userId, @DestinationVariable Long roundId, @RequestBody ClickRequest clickRequest) {
        playerFacade.saveClickLog(userId, roundId, clickRequest);
    }

    @Operation(
            summary = "게임입장 시 킬로그 publish 요청",
            description = "킬 로그에 대한 정보를 publish 요청하는 API 입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "반환 성공")
            },
            security = {@SecurityRequirement(name = "Access-Token")}
    )
    @MessageMapping("/topic/games/{roundId}/kill-log")
    public void getKillLog(@DestinationVariable("round-id") Long roundId) {
        playerFacade.getKillLog(roundId);
    }

}
