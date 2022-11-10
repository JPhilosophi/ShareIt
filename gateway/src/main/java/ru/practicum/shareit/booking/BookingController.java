package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClientExt bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") @NotNull Long bookerId,
                                                @RequestBody @Valid BookingInputDto bookingInputDto) {
        return bookingClient.create(bookerId, bookingInputDto);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") @NotNull Long bookerId,
                                   @PathVariable Long bookingId,
                                   @RequestParam(name = "approved", required = false) Boolean approved) {
        return bookingClient.update(bookerId, bookingId, approved);
    }


    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id")  @NotNull Long userId, @PathVariable Long bookingId) {
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllForBooker(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                         @RequestParam(name = "state", defaultValue = "ALL") BookingState stateParam,
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return bookingClient.getAllForBooker(userId, stateParam, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwner(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") BookingState stateParam,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return bookingClient.getAllByOwner(userId, stateParam, from, size);
    }
}
