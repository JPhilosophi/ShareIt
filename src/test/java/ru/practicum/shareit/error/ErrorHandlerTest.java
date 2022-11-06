package ru.practicum.shareit.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorHandlerTest {

    @Test
    void shouldBeDuplicateError() {
        ErrorHandler errorHandler = new ErrorHandler();
        DuplicateError duplicateError = new DuplicateError("dublicate error");
        ErrorResponse error = errorHandler.duplicateError(duplicateError);
        assertEquals(error.getError(), "Error: duplicate data");
    }

    @Test
    void shouldBeBadRequest() {
        ErrorHandler errorHandler = new ErrorHandler();
        BadRequestException exception = new BadRequestException("bad request");
        ErrorResponse error = errorHandler.handleObjectException(exception);
        assertEquals(error.getError(), "Unknown state: UNSUPPORTED_STATUS");
    }

    @Test
    void shouldBeNotFound() {
        ErrorHandler errorHandler = new ErrorHandler();
        NotFoundException exception = new NotFoundException("not found");
        ErrorResponse error = errorHandler.handleIdException(exception);
        assertEquals(error.getError(), "Can't found object with id");
    }
}
