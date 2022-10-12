package ru.practicum.shareit.booking;

public class BookingMapper {

    public static BookingEntity toBooking(BookingOutputDto bookingOutputDto, Long bookerId) {
        BookingEntity booking = new BookingEntity();
        booking.setId(bookingOutputDto.getId());
        booking.setStart(bookingOutputDto.getStart());
        booking.setEnd(bookingOutputDto.getEnd());
        booking.setItemId(bookingOutputDto.getItem_id());
        booking.setBookerId(bookingOutputDto.getBooker_id());
        return booking;
    }

    public static BookingOutputDto toBookingDto(BookingEntity bookingEntity) {
        BookingOutputDto booking = new BookingOutputDto();
        booking.setId(bookingEntity.getId());
        booking.setStart(bookingEntity.getStart());
        booking.setEnd(bookingEntity.getEnd());
        booking.setStatus(bookingEntity.getStatus());
        booking.setBooker_id(bookingEntity.getBookerId());
        booking.setItem_id(bookingEntity.getItemId());
        return booking;
    }
}
