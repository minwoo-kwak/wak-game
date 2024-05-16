package com.wak.game.domain.player.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@ToString
@NoArgsConstructor
public class PlayerInfo {
    private Long roundId;
    private Long userId;
    private String color;
    private String nickname;
    private String team;
    @JsonProperty("isHost")
    private boolean isHost;
    private int stamina;

    @Builder
    public PlayerInfo(Long roundId, Long userId, String color, String nickname, String team, boolean isHost, int stamina) {
        this.roundId = roundId;
        this.userId = userId;
        this.color = color;
        this.nickname = nickname;
        this.team = team;
        this.isHost = isHost;
        this.stamina = stamina;
    }

    public void updateStamina(int amount) {
        this.stamina += amount;
    }


    public Long getRoundId() {
        return roundId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getColor() {
        return color;
    }

    public String getNickname() {
        return nickname;
    }

    public String getTeam() {
        return team;
    }

    @JsonProperty("isHost")
    public boolean getIsHost() {
        return isHost;
    }

    public int getStamina() {
        return stamina;
    }
}


