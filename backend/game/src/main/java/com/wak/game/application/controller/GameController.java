package com.wak.game.application.controller;

import com.wak.game.application.facade.PlayerFacade;
import com.wak.game.application.facade.RoundFacade;
import com.wak.game.application.request.ClickRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class GameController {
    private final PlayerFacade playerFacade;

    @MessageMapping("/click/{roomId}")
    public void handleClick(@RequestBody ClickRequest clickRequest) {
        playerFacade.saveClickLog( clickRequest);
    }
}
