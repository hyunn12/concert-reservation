package io.hhplus.reserve.support.domain.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NOT_FOUND(404, "데이터를 찾을 수 없습니다."),
    INVALID_SEAT(400, "예약이 불가능한 좌석입니다."),
    EXPIRED_SEAT(400, "선점 만료된 좌석입니다."),
    INVALID_POINT(400, "포인트는 0보다 커야합니다."),
    INSUFFICIENT_POINT(400, "포인트가 부족합니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    INVALID_USER(403, "유효하지 않은 사용자입니다."),
    ;

    private final int code;
    private String message;

    ErrorCode(int code) {
        this.code = code;
    }

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
