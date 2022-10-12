package ru.practicum.shareit.booking;

public class BookingMapper {

    public static BookingEntity toBooking(BookingOutputDto bookingOutputDto, Long bookerId) {
        BookingEntity booking = new BookingEntity();
        booking.setId(bookingOutputDto.getId());
        booking.setStart(bookingOutputDto.getStart());
        booking.setEnd(bookingOutputDto.getEnd());
        booking.setItem_id(bookingOutputDto.getItem_id());
        booking.setBooker_id(bookingOutputDto.getBooker_id());
        return booking;
    }

    public static BookingOutputDto toBookingDto(BookingEntity bookingEntity) {
        BookingOutputDto booking = new BookingOutputDto();
        booking.setId(bookingEntity.getId());
        booking.setStart(bookingEntity.getStart());
        booking.setEnd(bookingEntity.getEnd());
        booking.setStatus(bookingEntity.getStatus());
        booking.setBooker_id(bookingEntity.getBooker_id());
        booking.setItem_id(bookingEntity.getItem_id());
        return booking;
    }
}
