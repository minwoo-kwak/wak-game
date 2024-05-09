package com.wak.game.application.vo;

import java.time.LocalDateTime;

public record clickVO(Long userId, Long victimId, Long roundId, LocalDateTime time) {
    public clickVO(Long userId, Long victimId, Long roundId, LocalDateTime time) {
        this.userId = userId;
        this.victimId = victimId;
        this.roundId = roundId;
        this.time = time;
    }
}