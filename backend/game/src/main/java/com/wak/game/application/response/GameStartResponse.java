package com.wak.game.application.response;

import java.util.List;

public record GameStartResponse(List<Long> players) {
    public static GameStartResponse of(List<Long> players) {
        return new GameStartResponse(players);
    }

    public GameStartResponse(List<Long> players) {
        this.players = players;
    }
}
