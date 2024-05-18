package com.wak.game.application.response.socket;

public record ResultResponse(long userId, int rank, int killCount, String aliveTime, String victim,
                             String victimColor) {
}

