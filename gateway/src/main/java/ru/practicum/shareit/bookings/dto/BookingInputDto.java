package ru.practicum.shareit.bookings.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingInputDto {
    private Long id;
    @JsonFormat
    private LocalDateTime start;
    @JsonFormat
    private LocalDateTime end;
}
