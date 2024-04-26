package com.wak.game.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GameController {
    //private final SimpMessageSendingOperations sendingOperations;
    //private final PlayerFacade playerFacade;

    @MessageMapping("/games/{roodId}/battle-field")
    @SendTo("/games/{roomId}/battle-field")
    public void getStockOrderInfo(@DestinationVariable Long roodId) {
        //토큰 검증

        //게임 구독!
        //레디스에 있는 룸 넘버가 roomId인 유저들 다 게임 현황 테이블(레디스)로 이동

        //List<AlivePlayerResponse> aliveUsers = playerFacade.getAliveUsers(roomRoundId);
        //룸에 있는 모든 유저에게 살아있는 유저들의 정보(위치좌표, 체력, 닉네임) 송신
        //sendingOperations.convertAndSend("/topic/room/" + roomRoundId, aliveUsers);
    }
}
