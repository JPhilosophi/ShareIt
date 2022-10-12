package ru.practicum.shareit.item;

import lombok.Data;
import ru.practicum.shareit.booking.ShortBookingDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemDto {
    @NotNull(message = "Id can't be null")
    private Long id = 0L;
    @NotEmpty(message = "Name can't be empty")
    private String name;
    @NotEmpty(message = "Description can't be empty")
    private String description;
    @NotNull
    private Boolean available;
    private Long owner;
    private Long request;
    private ShortBookingDto lastBooking;
    private ShortBookingDto nextBooking;
    private List<CommentDto> comments;
}
