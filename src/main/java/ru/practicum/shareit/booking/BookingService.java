package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {
    BookingOutputDto create(Long userId, BookingInputDto bookingInputDto);

    BookingOutputDto update(Long userId, Long bookingId, Boolean approved);

    List<BookingOutputDto> getAllByBooker(Long userId, State state);

    BookingOutputDto getById(Long userId, Long bookingId);

    List<BookingOutputDto> getAllByOwner(Long userId, State state);
}
