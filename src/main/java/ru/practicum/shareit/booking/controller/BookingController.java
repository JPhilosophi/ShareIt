package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.BookingInputDto;
import ru.practicum.shareit.booking.model.BookingOutputDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(@Qualifier("BookingRepository") BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingOutputDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingInputDto bookingInputDto) {
        return bookingService.create(userId, bookingInputDto);
    }

    @PatchMapping("{bookingId}")
    public BookingOutputDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable Long bookingId,
                                   @RequestParam(name = "approved", required = false) Boolean approved) {
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingOutputDto getById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingOutputDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(name = "state", required = false) State state) {
        return bookingService.getAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingOutputDto> getAllByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(name = "state", required = false) State state) {
        return bookingService.getAllByOwner(userId, state);
    }
}
