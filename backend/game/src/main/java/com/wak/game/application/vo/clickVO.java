package com.wak.game.application.vo;


public record clickVO(Long userId, Long victimId, Long roundId, String time) {
    public clickVO(Long userId, Long victimId, Long roundId, String time) {
        this.userId = userId;
        this.victimId = victimId;
        this.roundId = roundId;
        this.time = time;
    }
}