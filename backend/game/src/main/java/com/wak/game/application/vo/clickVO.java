package com.wak.game.application.vo;


public record clickVO(Long userId, Long victimId, Long roundId, String time, String nanoSec) {
    public clickVO(Long userId, Long victimId, Long roundId, String time, String nanoSec) {
        this.userId = userId;
        this.victimId = victimId;
        this.roundId = roundId;
        this.time = time;
        this.nanoSec = nanoSec;
    }
}