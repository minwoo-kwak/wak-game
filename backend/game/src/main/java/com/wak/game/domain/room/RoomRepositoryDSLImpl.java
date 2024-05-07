package com.wak.game.domain.room;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wak.game.domain.user.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.wak.game.domain.room.QRoom.room;

@RequiredArgsConstructor
public class RoomRepositoryDSLImpl implements RoomRepositoryDSL{

    private final JPAQueryFactory query;
    @Override
    public Optional<Room> findByUser(User user) {
        return Optional.ofNullable(query.select(room)
                .from(room)
                .where(room.user.eq(user))
                .where(room.isDeleted.eq(false))
                .fetchOne());
    }

    @Override
    public void deleteRoom(Long roomId) {
        query.update(room)
                .set(room.isDeleted, true)
                .set(room.deletedAt, LocalDateTime.now())
                .where(room.id.eq(roomId)).execute();
    }
}
