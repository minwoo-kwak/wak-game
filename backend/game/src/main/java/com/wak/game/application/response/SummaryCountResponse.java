package com.wak.game.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SummaryCountResponse {
    private final int roundNumber;
    private final int aliveCount;
    private final int totalCount;

    @Builder
    public SummaryCountResponse(int roundNumber, int aliveCount, int totalCount) {
        this.roundNumber = roundNumber;
        this.aliveCount = aliveCount;
        this.totalCount = totalCount;
    }
}
