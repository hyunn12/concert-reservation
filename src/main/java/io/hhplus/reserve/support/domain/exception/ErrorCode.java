package io.hhplus.reserve.support.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    UNAUTHORIZED,
    NOT_FOUND,
    VALIDATION_ERROR,
    SERVER_ERROR,
    CLIENT_ERROR,

}
