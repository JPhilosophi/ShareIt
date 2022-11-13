package ru.practicum.shareit.requests.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ItemRequestInputDto {
    @NotBlank
    @NotNull
    @NotEmpty
    private String description;
}
