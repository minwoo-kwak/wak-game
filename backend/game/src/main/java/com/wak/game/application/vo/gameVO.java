package com.wak.game.application.vo;

public record gameVO(Long userId, String color, String nickname, String team, boolean isHost, int stamina) {
    public gameVO(Long userId, String color, String nickname, String team, boolean isHost, int stamina) {
        this.userId = userId;
        this.color = color;
        this.nickname = nickname;
        this.team = team;
        this.isHost = isHost;
        this.stamina = stamina;
    }
}