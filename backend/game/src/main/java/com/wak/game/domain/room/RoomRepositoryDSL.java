package com.wak.game.domain.room;

import com.wak.game.domain.user.User;

import java.util.Optional;

public interface RoomRepositoryDSL {
    Optional<Room> findByUser(User user);
    void deleteRoom(Long roomId);
    void startGame(Long roomId);
    void endGame(Long roomId);
}
