package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.requests.model.ItemRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Item {
    private static Long count = 1L;
    @NotNull(message = "Id can't be null")
    private Long id = 0L;
    @NotNull(message = "Name can't be null")
    @NotEmpty(message = "Name can't be empty")
    private String name;
    @NotNull(message = "Description can't be null")
    @NotEmpty(message = "Description can't be empty")
    private String description;
    private Boolean available;
    private Long owner;
    private ItemRequest request;


    public void getNextId() {
        id = count++;
    }

    public ItemRequest getRequest() {
        return request;
    }

    public Boolean isAvailable() {
        return available;
    }
}
