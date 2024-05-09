package com.wak.game.application.vo;

public record gameVO(Long userId, String hexColor, String nickname, String team, boolean isChief, int stamina) {
    public gameVO(Long userId, String hexColor, String nickname, String team, boolean isChief, int stamina) {
        this.userId = userId;
        this.hexColor = hexColor;
        this.nickname = nickname;
        this.team = team;
        this.isChief = isChief;
        this.stamina = stamina;
    }
}