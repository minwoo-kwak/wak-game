package com.wak.game.application.response.socket;

import com.wak.game.domain.player.dto.PlayerInfo;

import java.util.List;

public record BattleFeildInGameResponse(boolean isFinished,List<PlayerInfo> players) {
    public BattleFeildInGameResponse(boolean isFinished, List<PlayerInfo> players) {
        this.isFinished = isFinished;
        this.players = players;
    }
}
