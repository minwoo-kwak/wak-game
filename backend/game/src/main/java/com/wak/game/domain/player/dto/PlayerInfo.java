package com.wak.game.domain.player.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PlayerInfo {
    private Long userId;
    private String color;
    private String nicKName;
    private String team;
    private boolean isHost;
    private int stamina;

    @Builder
    public PlayerInfo(Long userId, String color, String nicKName, String team, boolean isHost, int stamina) {
        this.userId = userId;
        this.color = color;
        this.nicKName = nicKName;
        this.team = team;
        this.isHost = isHost;
        this.stamina = stamina;
    }

    public void updateStamina(int amount) {
        this.stamina += amount;
    }
}


