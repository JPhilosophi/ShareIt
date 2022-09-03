package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.IItemDao;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.TreeSet;

@Slf4j
@Service
public class ItemService implements IItemService {
    private final IItemDao iItemDao;

    public ItemService(@Qualifier("memoryItem") IItemDao iItemDao) {
        this.iItemDao = iItemDao;
    }

    @Override
    public Item create(Long id, Item item) {
        return iItemDao.create(id, item);
    }

    @Override
    public Item update(Long userId, Long itemId, Item item) {
        return iItemDao.update(userId, itemId, item);
    }

    @Override
    public List<Item> get(Long userId) {
        return iItemDao.get(userId);
    }

    @Override
    public Item getById(Long userId, Long id) {
        return iItemDao.getById(userId, id);
    }

    @Override
    public TreeSet<Item> search(Long userId, String text) {
        return iItemDao.search(userId, text);
    }
}
