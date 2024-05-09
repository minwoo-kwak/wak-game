package com.wak.game.application.controller;

import com.wak.game.application.facade.PlayerFacade;
import com.wak.game.application.facade.RoundFacade;
import com.wak.game.application.request.ClickRequest;
import com.wak.game.application.response.DashBoardResponse;
import com.wak.game.application.vo.clickVO;
import com.wak.game.global.token.AuthUser;
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

    private final SimpMessagingTemplate messagingTemplate;
    private final RoundFacade roundFacade;
    private final PlayerFacade playerFacade;
    @MessageMapping("/topic/games/{roundId}/click")
    public void  click(@AuthUser Long userId, @DestinationVariable Long roundId, @RequestBody ClickRequest clickRequest) {
        //레디스에 적재
        playerFacade.saveClickLog(userId, roundId, clickRequest);
        //messagingTemplate.convertAndSend("/topic/games/" + roundId + "dashboard", result);
    }
}
