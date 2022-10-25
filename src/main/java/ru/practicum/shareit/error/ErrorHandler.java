package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    private ErrorResponse duplicateError(final DuplicateError e) {
        log.error("Error: duplicate data");
        return new ErrorResponse("Error: duplicate data");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorResponse handleObjectException(final BadRequestException e) {
        log.error("Error: something wrong with Object");
        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse handleIdException(final NotFoundException e) {
        log.error("Error: can't found object with id");
        return new ErrorResponse("Can't found object with id ");
    }
}
