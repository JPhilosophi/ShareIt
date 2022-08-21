package ru.practicum.shareit.error;

public class DuplicateError extends RuntimeException {
    public DuplicateError(String message) {
        super(message);
    }

}
