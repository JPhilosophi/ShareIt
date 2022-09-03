package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.TreeSet;

public interface IItemService {

    Item create(Long id, Item item);

    Item update(Long userId, Long itemId, Item item);

    List<Item> get(Long userId);

    Item getById(Long userId, Long itemId);

    TreeSet<Item> search(Long userId, String text);
}
