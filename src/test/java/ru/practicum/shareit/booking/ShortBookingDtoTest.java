package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.ShortBookingDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ShortBookingDtoTest {
    @Test
    void shortBookingTest() {
        ShortBookingDto shortBookingDto = new ShortBookingDto();
        shortBookingDto.setId(1L);
        shortBookingDto.setBookerId(1L);
        assertEquals(shortBookingDto.getId(), 1L);
        assertEquals(shortBookingDto.getBookerId(), 1L);
    }
}
