package com.wak.game.domain.round;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.wak.game.domain.round.QRound.round;

@RequiredArgsConstructor
public class RoundRepositoryDSLImpl implements RoundRepositoryDSL{

    private final JPAQueryFactory query;
    @Override
    public void deleteRound(Long roundId) {
        query.update(round)
                .set(round.isDeleted, true)
                .where(round.id.eq(roundId)).execute();
    }
}
