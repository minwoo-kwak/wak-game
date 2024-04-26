package com.wak.game.application.facade;

import com.wak.game.domain.color.Color;
import com.wak.game.domain.color.ColorService;
import com.wak.game.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserFacade {
    private final UserService userService;
    private final ColorService colorService;


    public void enterGame(String nickname) {
        Color color = colorService.findById(50);
        userService.save(nickname, color);
    }

}
