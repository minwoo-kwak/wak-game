package com.wak.game.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DashBoardResponse {
    private final Long roundId;
    private final int aliveCount;
    private final int totalCount;

    @Builder
    public DashBoardResponse(Long roundId, int aliveCount, int totalCount) {
        this.roundId = roundId;
        this.aliveCount = aliveCount;
        this.totalCount = totalCount;
    }
}
