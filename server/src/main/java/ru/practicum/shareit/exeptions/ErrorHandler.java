package ru.practicum.shareit.exeptions;

import lombok.Generated;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
@Generated
public class ErrorHandler {
    @ExceptionHandler({EntityNotFoundException.class})
    public void handlerNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler({ValidationException.class})
    public void handlerBadValidation(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handlerBadRequest(final IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(e.getMessage()));
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public void handlerInternalServerError(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
