package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.converter.StringToEnumConverter;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.error.BadRequestException;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class StringToEnumConverterTest {
    @Test
    void convert() {
        String status = "CURRENT";
        StringToEnumConverter stringToEnumConverter = new StringToEnumConverter();
        assertEquals(stringToEnumConverter.convert(status), State.CURRENT);
    }

    @Test
    void shouldBeError() {
        StringToEnumConverter stringToEnumConverter = new StringToEnumConverter();
        assertThrows(BadRequestException.class, () -> stringToEnumConverter.convert("SOOON"));
    }
}
