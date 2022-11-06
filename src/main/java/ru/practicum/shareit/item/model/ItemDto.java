package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.booking.model.ShortBookingDto;

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
    private Long requestId;
    private ShortBookingDto lastBooking;
    private ShortBookingDto nextBooking;
    private List<CommentDto> comments;
}
