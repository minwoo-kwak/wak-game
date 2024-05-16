package com.wak.game.application.response.socket;

public record RoundInfoResponse (Long roundId){
    public RoundInfoResponse(Long roundId) {
        this.roundId = roundId;
    }
}
