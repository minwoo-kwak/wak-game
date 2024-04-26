package com.wak.game.global.error.exception;

import com.wak.game.global.error.ErrorInfo;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorInfo errorInfoCode;

    public BusinessException(ErrorInfo errorInfoCode) {
        super(errorInfoCode.getMessage());
        this.errorInfoCode = errorInfoCode;
    }

}
