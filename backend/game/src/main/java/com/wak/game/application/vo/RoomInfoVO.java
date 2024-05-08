package com.wak.game.application.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoomInfoVO {

    private long room_id;
    private String room_name;
    private int current_players;
    private int limit_players;
    private String mode;
    private boolean start;
    private boolean lock;

    public int updateCurrentPlayers() {
        return ++current_players;
    }
    public int decreaseCurrentPlayers() {
        return --current_players;
    }
}