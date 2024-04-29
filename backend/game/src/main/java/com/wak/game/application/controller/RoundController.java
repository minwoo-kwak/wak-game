package com.wak.game.application.controller;

import com.wak.game.application.controller.dto.AlivePlayerResponse;
import com.wak.game.application.controller.dto.GameStartRequest;
import com.wak.game.application.facade.RoundFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class RoundController {
    private final SimpMessagingTemplate messagingTemplate;
    private final RoundFacade roundFacade;

    @PostMapping("/start/{roomId}")
    public void startGame(@RequestBody GameStartRequest gameStartRequest, @DestinationVariable Long roomId) {
        /**
         *  방장 유효성 검증 로직 추가
         */
        List<AlivePlayerResponse> aliveUsers = roundFacade.startGameAndReturnStatus(gameStartRequest, roomId);
        messagingTemplate.convertAndSend("/topic/games/" + roomId + "/battle-field", aliveUsers);
    }

    @MessageMapping("/{roomId}/battle-field")
    public void getBattleField(@RequestBody GameStartRequest gameStartRequest, @DestinationVariable Long roomId) {
        List<AlivePlayerResponse> aliveUsers = roundFacade.startGameAndReturnStatus(gameStartRequest, roomId);
        messagingTemplate.convertAndSend("/topic/games/" + roomId + "/battle-field", aliveUsers);
    }
    @MessageMapping("/{roomId}/dashboard")
    public void getDashBoard(@RequestBody GameStartRequest gameStartRequest, @DestinationVariable Long roomId) {
        List<AlivePlayerResponse> aliveUsers = roundFacade.startGameAndReturnStatus(gameStartRequest, roomId);
        messagingTemplate.convertAndSend("/topic/games/" + roomId + "dashboard", aliveUsers);
    }
    @MessageMapping("/{roomId}/kill-log")
    public void getKillLog(@RequestBody GameStartRequest gameStartRequest, @DestinationVariable Long roomId) {
        List<AlivePlayerResponse> aliveUsers = roundFacade.startGameAndReturnStatus(gameStartRequest, roomId);
        messagingTemplate.convertAndSend("/topic/games/" + roomId + "kill-log", aliveUsers);
    }
    @MessageMapping("/{roomId}/mention")
    public void mention(@RequestBody GameStartRequest gameStartRequest, @DestinationVariable Long roomId) {
        List<AlivePlayerResponse> aliveUsers = roundFacade.startGameAndReturnStatus(gameStartRequest, roomId);
        messagingTemplate.convertAndSend("/topic/games/" + roomId + "mention", aliveUsers);
    }


}
