package com.wak.game.application.controller;

import com.wak.game.application.facade.PlayerFacade;
import com.wak.game.application.facade.RoundFacade;
import com.wak.game.application.request.ClickRequest;
import com.wak.game.global.token.AuthUser;
import com.wak.game.global.util.ApiResult;
import com.wak.game.global.util.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class GameController {
    private final PlayerFacade playerFacade;
    private final RoundFacade roundFacade;

    @MessageMapping("/click/{roundId}")
    public void handleClick(ClickRequest clickRequest) {
        playerFacade.saveClickLog(clickRequest);
    }
}
