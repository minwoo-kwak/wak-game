package com.wak.game.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wak.game.domain.color.Color;
import lombok.RequiredArgsConstructor;

import static com.wak.game.domain.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryDSLImpl implements UserRepositoryDSL{

    private final JPAQueryFactory query;
    @Override
    public User findByUserInfo(String nickname, Color color) {
        return query.select(user)
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetchOne();
    }

    @Override
    public long countByNickname(String nickname) {
        return query.select(user)
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetchCount();
    }
}
