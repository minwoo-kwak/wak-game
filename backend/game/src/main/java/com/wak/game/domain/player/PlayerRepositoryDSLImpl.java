package com.wak.game.domain.player;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wak.game.domain.room.Room;
import com.wak.game.domain.room.RoomRepositoryDSL;
import com.wak.game.domain.user.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.wak.game.domain.room.QRoom.room;

@RequiredArgsConstructor
public class PlayerRepositoryDSLImpl implements PlayerRepositoryDSL {

    private final JPAQueryFactory query;

}
