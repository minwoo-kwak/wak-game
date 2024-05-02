package com.wak.game.domain.room;

import com.wak.game.domain.user.User;

import java.util.Optional;

public interface RoomRepositoryDSL {
    Optional<Room> findByUser(User user);

}