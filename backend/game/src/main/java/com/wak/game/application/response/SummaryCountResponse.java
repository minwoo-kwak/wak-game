package com.wak.game.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SummaryCountResponse {
    private final int aliveCount;
    private final int totalCount;

    @Builder
    public SummaryCountResponse(int aliveCount, int totalCount) {
        this.aliveCount = aliveCount;
        this.totalCount = totalCount;
    }
}
