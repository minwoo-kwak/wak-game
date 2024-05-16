package com.wak.game.application.response.socket;

public record ResultResponse(int roundNumber, int killCount/* todo String aliveTime, int rank, String victim, String victimColor*/) {
    public ResultResponse(int roundNumber, int killCount) {
        this.roundNumber = roundNumber;
        this.killCount = killCount;
    }
    /* todo
    public ResultResponse(int roundNumber, String aliveTime, int killCount, int rank, String victim, String victimColor) {
        this.roundNumber = roundNumber;
        this.killCount = killCount;
        this.aliveTime = aliveTime;
        this.rank = rank;
        this.victim = victim;
        this.victimColor = victimColor;
    }*/
}

