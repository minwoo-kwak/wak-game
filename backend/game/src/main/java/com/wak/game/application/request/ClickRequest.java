package com.wak.game.application.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ClickRequest {
    private final Long roundId;
    private final Long userId;
    private final Long victimId;
    private final LocalDateTime clickTime;

    @Builder
    public ClickRequest(Long roundId, Long userId, Long victimId, LocalDateTime clickTime) {
        this.roundId = roundId;
        this.userId = userId;
        this.victimId = victimId;
        this.clickTime = clickTime;
    }
}
