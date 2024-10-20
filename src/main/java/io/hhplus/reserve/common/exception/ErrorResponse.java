package io.hhplus.reserve.common.exception;

public record ErrorResponse(
        int code,
        String message
) {
}
