package com.wak.game.application.facade;

import com.wak.game.application.request.UserLogInRequest;
import com.wak.game.application.response.UserInfoResponse;
import com.wak.game.application.response.UserLogInResponse;
import com.wak.game.domain.color.Color;
import com.wak.game.domain.color.ColorService;
import com.wak.game.domain.user.User;
import com.wak.game.domain.user.UserService;
import com.wak.game.global.token.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserFacade {
    private final UserService userService;
    private final ColorService colorService;
    private final JWTUtils jwtUtils;

    @Transactional
    public UserLogInResponse login(UserLogInRequest request) {
        Random random = new Random();
        int color_id = random.nextInt(10) + 1;
        Color color = colorService.findById(color_id);

        User user = userService.save(request.nickname(), color);
        String token = jwtUtils.createJwt(user.getId());
        return UserLogInResponse.of(token, color.getHexColor());
    }

    public UserInfoResponse userInfo(long user_id) {
        User user = userService.findById(user_id);
        return UserInfoResponse.of(user.getNickname());
    }
}
