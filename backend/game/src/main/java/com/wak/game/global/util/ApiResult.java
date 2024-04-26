package com.wak.game.global.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ApiResult<T> {

    private final boolean success;
    private final T data;
    private final ApiError error;

}

