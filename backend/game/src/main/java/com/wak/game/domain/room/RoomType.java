package com.wak.game.domain.room;

import com.wak.game.global.error.ErrorInfo;
import com.wak.game.global.error.exception.BusinessException;

public enum RoomType {
    SOLO("SOLO"),
    TEAM("TEAM");

    private final String mode;

    RoomType(String mode) {
        this.mode = mode;
    }

    public static RoomType of(String mode) {
        if (mode == null)
            throw new BusinessException(ErrorInfo.ROOM_MODE_NOT_EXIST);

        for (RoomType rt : RoomType.values()) {
            if (rt.mode.equals(mode))
                return rt;
        }

        throw new BusinessException(ErrorInfo.ROOM_MODE_NOT_EXIST);
    }

    @Override
    public String toString() {
        return mode;
    }
}
