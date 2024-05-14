package com.wak.game.application.controller;

import com.wak.game.application.facade.PlayerFacade;
import com.wak.game.application.request.ClickRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameController {
    private final PlayerFacade playerFacade;

    @MessageMapping("/click/{roundId}")
    public void handleClick(ClickRequest clickRequest) {
        playerFacade.saveClickLog(clickRequest);
    }
}
