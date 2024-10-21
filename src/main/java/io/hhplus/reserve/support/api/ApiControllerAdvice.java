package io.hhplus.reserve.support.api;

import io.hhplus.reserve.support.domain.exception.BusinessException;
import io.hhplus.reserve.support.domain.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
class ApiControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        switch (e.getErrorType().getLogLevel()) {
            case ERROR -> log.error(e.getErrorType().getMessage(), e);
            case WARN -> log.warn(e.getErrorType().getMessage(), e);
            default -> log.info(e.getErrorType().getMessage(), e);
        }

        HttpStatus status;
        switch (e.getErrorType().getErrorCode()) {
            case UNAUTHORIZED -> status = HttpStatus.UNAUTHORIZED;
            case NOT_FOUND -> status = HttpStatus.NOT_FOUND;
            case SERVER_ERROR -> status = HttpStatus.INTERNAL_SERVER_ERROR;
            case CLIENT_ERROR -> status = HttpStatus.BAD_REQUEST;
            default -> status = HttpStatus.OK;
        }

        ErrorResponse errorResponse = new ErrorResponse(status.value(), e.getErrorType().getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return ResponseEntity.status(500).body(new ErrorResponse(500, e.getLocalizedMessage()));
    }

}
