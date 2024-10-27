package io.hhplus.reserve.support.domain.exception;

public record ErrorResponse(
        int code,
        String message
) {
}
