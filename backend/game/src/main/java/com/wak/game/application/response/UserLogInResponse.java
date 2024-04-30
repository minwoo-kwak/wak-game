package com.wak.game.application.response;

public record UserLogInResponse(String token, String color){

    public static UserLogInResponse of (String token, String color) {
        return new UserLogInResponse(token, color);
    }

    public UserLogInResponse(String token, String color){
        this.color = color;
        this.token = token;
    }
}
