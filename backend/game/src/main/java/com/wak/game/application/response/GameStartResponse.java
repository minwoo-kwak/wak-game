package com.wak.game.application.response;

import java.util.List;

public record GameStartResponse(Long roundId) {
    public static GameStartResponse of(Long roundId) {
        return new GameStartResponse(roundId);
    }

    public GameStartResponse(Long roundId) {
        this.roundId = roundId;
    }
}
