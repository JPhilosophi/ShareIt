package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentInto {
    private Long id;
    @NotBlank
    private String text;
    private Long userId;
    private Long itemId;
}
