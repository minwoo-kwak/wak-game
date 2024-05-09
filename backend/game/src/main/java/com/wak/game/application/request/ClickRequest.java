package com.wak.game.application.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ClickRequest {
    private final Long victimId;
    private final Long roundId;
    private final LocalDateTime clickTime;

    @Builder
    public ClickRequest(Long victimId, Long roundId, LocalDateTime clickTime) {
        this.victimId = victimId;
        this.roundId = roundId;
        this.clickTime = clickTime;
    }
}
