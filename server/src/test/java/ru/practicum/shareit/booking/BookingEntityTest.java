package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.BookingEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BookingEntityTest {
    @Test
    void bookingEntityCreate() {
        BookingEntity bookingEntity1 = new BookingEntity();
        BookingEntity bookingEntity2 = new BookingEntity();
        bookingEntity1.setId(1L);
        bookingEntity2.setId(2L);
        bookingEntity1.setStart(LocalDateTime.now().plusDays(1));
        bookingEntity2.setStart(LocalDateTime.now().plusDays(2));
        assertEquals(1, bookingEntity1.compareTo(bookingEntity2));
        bookingEntity1.setStart(LocalDateTime.now());
        bookingEntity2.setStart(LocalDateTime.now());
        assertEquals(1, bookingEntity1.compareTo(bookingEntity2));
     }
}
