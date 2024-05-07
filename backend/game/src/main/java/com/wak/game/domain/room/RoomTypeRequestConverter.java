package com.wak.game.domain.room;

import org.springframework.core.convert.converter.Converter;

public class RoomTypeRequestConverter implements Converter<String, RoomType> {
    @Override
    public RoomType convert(String source) {
        return RoomType.of(source);
    }
}
