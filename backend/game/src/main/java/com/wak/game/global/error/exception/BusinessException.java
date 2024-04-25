package com.wak.game.global.error.exception;

import com.wak.game.global.error.Error;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final Error error;

    public BusinessException(Error error) {
        super(error.getMessage());
        this.error = error;
    }

}
