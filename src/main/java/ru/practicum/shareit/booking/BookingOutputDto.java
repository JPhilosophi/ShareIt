package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.ShortItemDto;
import ru.practicum.shareit.user.ShortUserDto;

import java.time.LocalDateTime;

@Data
public class BookingOutputDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;
    private ShortUserDto booker;
    private ShortItemDto item;
    private Long bookerId;
    private Long itemId;
}
