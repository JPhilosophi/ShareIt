package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.CommentDto;
import ru.practicum.shareit.item.model.CommentInto;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long id, ItemDto item);

    ItemDto update(Long userId, Long itemId, ItemDto item);

    List<ItemDto> get(Long userId);

    ItemDto getById(Long userId, Long itemId);

    List<ItemDto> search(Long userId, String text);

    CommentDto createComment(CommentInto comment, Long userId, Long itemId);
}
