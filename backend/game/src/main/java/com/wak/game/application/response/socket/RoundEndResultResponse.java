package com.wak.game.application.response.socket;

import java.util.List;

public record RoundEndResultResponse (boolean isFinished, int roundNumber, Long nextRoundId, List<ResultResponse> results){
    public RoundEndResultResponse(boolean isFinished, int roundNumber, Long nextRoundId, List<ResultResponse> results) {
        this.isFinished = isFinished;
        this.roundNumber = roundNumber;
        this.nextRoundId = nextRoundId;
        this.results = results;
    }
}
