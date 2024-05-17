package com.wak.game.application.response.socket;

public record ResultResponse(long userId, int rank,
                             int killCount /* todo String aliveTime, int rank, String victim, String victimColor*/) {
    public ResultResponse(long userId, int rank, int killCount) {
        this.userId = userId;
        this.killCount = killCount;
        this.rank = rank;
    }
    /* todo
    public ResultResponse(int roundNumber, String aliveTime, int killCount, int rank, String victim, String victimColor) {
        this.roundNumber = roundNumber;
        this.killCount = killCount;
        this.aliveTime = aliveTime;
        this.rank = rank;
        this.victim = victim;
        this.victimColor = victimColor;
        this.isFinish = isFinish;

    }*/
}

