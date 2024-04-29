package com.wak.game.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.wak.game.domain.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryDSLImpl implements UserRepositoryDSL{

    private final JPAQueryFactory query;
    @Override
    public Optional<User> findByNickname(String nickname) {
        return Optional.ofNullable(query.select(user)
                .from(user)
                .where(user.nickname.eq(nickname))
                .where(user.isDeleted.eq(false))
                .fetchOne());
    }

    @Override
    public long countByNickname(String nickname) {
        return query.select(user)
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetchCount();
    }
}
