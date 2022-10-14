package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.shareit.item.model.ShortItemDto;
import ru.practicum.shareit.user.model.ShortUserDto;

import java.time.LocalDateTime;

@Data
public class BookingOutputDto {
    private Long id;
    @JsonFormat
    private LocalDateTime start;
    @JsonFormat
    private LocalDateTime end;
    private Status status;
    private ShortUserDto booker;
    private ShortItemDto item;
    private Long bookerId;
    private Long itemId;
}
