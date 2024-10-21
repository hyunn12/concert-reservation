package io.hhplus.reserve.support.domain.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorType errorType;
    private Object payload;

    public BusinessException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public BusinessException(ErrorType errorType, Object payload) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.payload = payload;
    }
}
