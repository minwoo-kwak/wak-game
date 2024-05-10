package com.wak.game.application.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class RoomInfoVO {

    private long roomId;
    private String roomName;
    private int currentPlayers;
    private int limitPlayers;
    private String mode;
    @JsonProperty("isStart")
    private boolean isStart;
    @JsonProperty("isPublic")
    private boolean isPublic;

    public int updateCurrentPlayers() {
        return ++currentPlayers;
    }
    public int decreaseCurrentPlayers() {
        return --currentPlayers;
    }
    public void gameStart() {
        this.isStart = true;
    }
    public void gameFinish() {
        this.isStart = false;
    }
    public long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public int getLimitPlayers() {
        return limitPlayers;
    }

    public String getMode() {
        return mode;
    }

    @JsonProperty("isStart")
    public boolean getIsStart() {
        return isStart;
    }
    @JsonProperty("isPublic")
    public boolean getIsPublic() {
        return isPublic;
    }
}