package ru.practicum.shareit.items.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
}
