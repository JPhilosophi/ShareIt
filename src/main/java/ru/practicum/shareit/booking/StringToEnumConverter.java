package ru.practicum.shareit.booking;


import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.error.BadRequestException;

public class StringToEnumConverter implements Converter<String, State> {
    @Override
    public State convert(String state) {
        try {
            return State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
