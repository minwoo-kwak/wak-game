package com.wak.game.domain.round.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PlayerCount {
    private int totalCountA;
    private int aliveCountA;
    private int totalCountB;
    private int aliveCountB;

    @Builder
    public PlayerCount(int totalCountA, int aliveCountA, int totalCountB, int aliveCountB) {
        this.totalCountA = totalCountA;
        this.aliveCountA = aliveCountA;
        this.totalCountB = totalCountB;
        this.aliveCountB = aliveCountB;
    }

    public void updateAliveCont() {
        --this.aliveCountA;
    }
}