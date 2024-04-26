package com.wak.game.domain.color;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.wak.game.domain.color.QColor.color;


@RequiredArgsConstructor
public class ColorRepositoryCustomImpl implements ColorRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Color findByHexColor(String hexColor) {

        return query.select(color)
                .from(color)
                .where(color.hexColor.eq(hexColor))
                .fetchOne();
    }

}
