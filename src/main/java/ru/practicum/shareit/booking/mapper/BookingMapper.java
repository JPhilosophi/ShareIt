package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.model.BookingAnswer;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.booking.model.BookingOutputDto;

public class BookingMapper {

    public static BookingEntity toBooking(BookingOutputDto bookingOutputDto, Long bookerId) {
        BookingEntity booking = new BookingEntity();
        booking.setId(bookerId);
        booking.setStart(bookingOutputDto.getStart());
        booking.setEnd(bookingOutputDto.getEnd());
        booking.setItemId(bookingOutputDto.getItemId());
        booking.setBookerId(bookingOutputDto.getBookerId());
        return booking;
    }

    public static BookingAnswer toBookingDto(BookingEntity bookingEntity) {
        BookingAnswer booking = new BookingAnswer();
        booking.setId(bookingEntity.getId());
        booking.setStart(bookingEntity.getStart());
        booking.setEnd(bookingEntity.getEnd());
        booking.setStatus(bookingEntity.getStatus());
        booking.setItemId(bookingEntity.getItemId());
        return booking;
    }


}
