package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    private Long userId;
    private Long itemId;
}
