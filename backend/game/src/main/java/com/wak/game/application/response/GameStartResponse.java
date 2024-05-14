package com.wak.game.application.response;

import java.util.List;

public record GameStartResponse(Long roundId, List<Long> players) {
    public static GameStartResponse of(Long roundId, List<Long> players) {
        return new GameStartResponse(roundId, players);
    }

    public GameStartResponse(Long roundId, List<Long> players) {
        this.roundId = roundId;
        this.players = players;
    }
}
