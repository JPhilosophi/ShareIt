package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.State;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class StringToEnumConverterTest {
    @Test
    void convert() {
        String status = "CURRENT";
        assertEquals(State.CURRENT, State.valueOf(status.toUpperCase()));
    }
}
