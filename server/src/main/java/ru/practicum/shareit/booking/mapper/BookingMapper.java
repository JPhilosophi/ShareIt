package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingInputDto;
import ru.practicum.shareit.booking.model.BookingOutputDto;

public class BookingMapper {

    public static BookingEntity toBooking(BookingInputDto bookingInputDto, Long bookerId) {
        BookingEntity booking = new BookingEntity();
        booking.setBookerId(bookerId);
        booking.setItemId(bookingInputDto.getItemId());
        booking.setStart(bookingInputDto.getStart());
        booking.setEnd(bookingInputDto.getEnd());
        return booking;
    }

    public static BookingOutputDto toBookingDto(BookingEntity bookingEntity) {
        BookingOutputDto booking = new BookingOutputDto();
        booking.setId(bookingEntity.getId());
        booking.setStart(bookingEntity.getStart());
        booking.setEnd(bookingEntity.getEnd());
        booking.setStatus(bookingEntity.getStatus());
        return booking;
    }


}
