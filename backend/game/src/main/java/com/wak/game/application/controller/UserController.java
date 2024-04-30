package com.wak.game.application.controller;

import com.wak.game.application.facade.UserFacade;
import com.wak.game.application.request.UserLogInRequest;
import com.wak.game.application.response.UserInfoResponse;
import com.wak.game.application.response.UserLogInResponse;
import com.wak.game.global.token.AuthUser;
import com.wak.game.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("")
    public ResponseEntity<?> login(@RequestBody UserLogInRequest request) {
        UserLogInResponse response = userFacade.login(request);
        return ResponseEntity.ok(ApiUtils.success(response));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@AuthUser Long id){
        UserInfoResponse response = userFacade.userInfo(id);
        return ResponseEntity.ok(ApiUtils.success(response));
    }

}
