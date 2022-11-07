package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingInputDto {
    @NotNull
    private Long itemId;
    @NotNull
    @Future
    @JsonFormat
    private LocalDateTime start;
    @NotNull
    @Future
    @JsonFormat
    private LocalDateTime end;
}
