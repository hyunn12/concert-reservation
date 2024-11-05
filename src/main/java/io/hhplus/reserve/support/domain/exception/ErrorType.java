package io.hhplus.reserve.support.domain.exception;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
public enum ErrorType {

    INTERNAL_SERVER_ERROR(ErrorCode.SERVER_ERROR, "문제가 발생하였습니다.", LogLevel.ERROR),
    RESOURCE_NOT_FOUNT(ErrorCode.NOT_FOUND, "데이터를 찾을 수 없습니다.", LogLevel.WARN),
    INVALID_TOKEN(ErrorCode.UNAUTHORIZED, "토큰이 유효하지 않습니다.", LogLevel.ERROR),
    BAD_POINT_REQUEST(ErrorCode.VALIDATION_ERROR, "포인트는 0보다 커야합니다.", LogLevel.WARN),
    INSUFFICIENT_POINT(ErrorCode.VALIDATION_ERROR, "포인트가 부족합니다.", LogLevel.WARN),
    INVALID_SEAT(ErrorCode.VALIDATION_ERROR, "예약 불가능한 좌석입니다.", LogLevel.WARN),
    EXPIRED_SEAT(ErrorCode.VALIDATION_ERROR, "좌석 선점이 만료되었습니다.", LogLevel.WARN),

    WAITING_KEY_BAD_REQUEST(ErrorCode.BAD_REQUEST, "키에 해당하는 대기열을 찾을 수 없습니다.", LogLevel.ERROR),
    ;

    private final ErrorCode errorCode;
    private final String message;
    private final LogLevel logLevel;

    ErrorType(ErrorCode errorCode, String message, LogLevel logLevel) {
        this.errorCode = errorCode;
        this.message = message;
        this.logLevel = logLevel;
    }
}
