package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.BookingInputDto;
import ru.practicum.shareit.booking.model.BookingOutputDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingOutputDto create(Long userId, BookingInputDto bookingInputDto);

    BookingOutputDto update(Long userId, Long bookingId, Boolean approved);

    List<BookingOutputDto> getAllByBooker(Long userId, State state, Integer from, Integer size);

    BookingOutputDto getById(Long userId, Long bookingId);

    List<BookingOutputDto> getAllByOwner(Long userId, State state, Integer from, Integer size);
}
