package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingInputDto;
import ru.practicum.shareit.booking.model.BookingOutputDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.item.model.ShortItemDto;
import ru.practicum.shareit.user.model.ShortUserDto;
import ru.practicum.shareit.user.model.UserEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BookingMapperTest {

    @InjectMocks
    private BookingInputDto bookingInputDto;
    private BookingOutputDto bookingOutputDto;
    private BookingEntity booking;
    private ItemEntity item;
    private UserEntity booker;
    private ShortUserDto shortUserDto;
    private ShortItemDto shortItemDto;
    private UserEntity userEntity;
    private ItemEntity itemEntity;

    @BeforeEach
    void setUp() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        shortUserDto = new ShortUserDto(1L);
        shortItemDto = new ShortItemDto(2L, "Санки");

        booker = new UserEntity();
        booker.setId(1L);
        booker.setName("Vlad");
        booker.setEmail("blad@vlad.ru");

        item = new ItemEntity();
        item.setId(1L);

        bookingInputDto = new BookingInputDto();
        bookingInputDto.setItemId(1L);
        bookingInputDto.setStart(start);
        bookingInputDto.setEnd(end);

        booking = new BookingEntity();
        booking.setId(1L);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setStatus(Status.WAITING);

        bookingOutputDto = new BookingOutputDto();
        bookingOutputDto.setId(booking.getId());
        bookingOutputDto.setStart(booking.getStart());
        bookingOutputDto.setEnd(booking.getEnd());
        bookingOutputDto.setStatus(booking.getStatus());

        itemEntity = new ItemEntity();
        itemEntity.setId(2L);
        itemEntity.setName("Сноуборд");
        itemEntity.setDescription("Новый сноуборд");
        itemEntity.setOwnerId(1L);
        itemEntity.setRequestId(1L);
        itemEntity.setAvailable(true);
        itemEntity.setNextBookingId(1L);
        itemEntity.setLastBookingId(2L);

        userEntity = new UserEntity();
        userEntity.setId(2L);
        userEntity.setName("Name");
        userEntity.setEmail("emai@email.com");
    }

    @Test
    void toBooking() {
        BookingEntity testBooking = BookingMapper.toBooking(bookingInputDto, booker.getId());
        assertEquals(bookingInputDto.getStart(), testBooking.getStart());
        assertEquals(bookingInputDto.getEnd(), testBooking.getEnd());
    }

    @Test
    void toBookingDto() {
        BookingOutputDto testBookingOutputDto = BookingMapper.toBookingDto(booking);
        assertEquals(booking.getId(), testBookingOutputDto.getId());
        assertEquals(booking.getStart(), testBookingOutputDto.getStart());
        assertEquals(booking.getEnd(), testBookingOutputDto.getEnd());
        assertEquals(booking.getStatus(), testBookingOutputDto.getStatus());
    }
}
