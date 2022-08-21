package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.IItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Service
public class ItemService implements IItemService{
    private final IItemDto iItemDto;

    public ItemService(@Qualifier("memoryItem")IItemDto iItemDto) {
        this.iItemDto = iItemDto;
    }

    @Override
    public Item create(Long id, Item item) {
        return iItemDto.create(id, item);
    }

    @Override
    public Item update(Long userId, Long itemId, Item item) {
        return iItemDto.update(userId, itemId, item);
    }

    @Override
    public List<Item> get(Long userId) {
        return iItemDto.get(userId);
    }

    @Override
    public Item getById(Long userId, Long id) {
        return iItemDto.getById(userId, id);
    }

    @Override
    public TreeSet<Item> search(Long userId, String text) {
        return iItemDto.search(userId, text);
    }
}
