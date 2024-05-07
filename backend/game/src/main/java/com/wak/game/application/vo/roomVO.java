package com.wak.game.application.vo;

public record roomVO(Long userId, String hexColor, String nickname, String team, boolean isChief) {
    public roomVO(Long userId, String hexColor, String nickname, String team, boolean isChief) {
        this.userId = userId;
        this.hexColor = hexColor;
        this.nickname = nickname;
        this.team = team;
        this.isChief = isChief;
    }
}
