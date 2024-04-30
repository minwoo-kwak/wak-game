package com.wak.game.application.request;

import jakarta.validation.constraints.NotBlank;

public record UserLogInRequest(@NotBlank String nickname){
    public UserLogInRequest(String nickname){
        this.nickname = nickname;
    }

}
