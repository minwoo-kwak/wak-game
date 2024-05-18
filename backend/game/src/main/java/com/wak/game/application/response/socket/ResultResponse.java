package com.wak.game.application.response.socket;

public record ResultResponse(long userId, int rank, int killCount, int playTime, double aliveTime, String victim,
                             String victimColor) {
}

