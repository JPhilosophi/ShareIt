package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.BadRequestException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.IUserDao;

import java.util.*;

@Component("memoryItem")
public class ItemDao implements IItemDao {
    private final Map<Long, Item> userItems = new HashMap<>();
    private final IUserDao userDao;

    public ItemDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Item create(Long id, Item item) {
        if (!userDao.checkUserInStorage().containsKey(id)) {
            throw new NotFoundException("Can't found user with id " + id);
        }
        if (item.getAvailable() == null) {
            throw new BadRequestException("available mast be");
        }
        item.getNextId();
        item.setOwner(id);
        userItems.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Long userId, Long itemId, Item item) {
        if (!userItems.get(itemId).getOwner().equals(userId)) {
            throw new NotFoundException("You try change not your item");
        }
        List<Item> itemList = new ArrayList<>(userItems.values());
        for (Item item1 : itemList) {
            if (item1.getId().equals(itemId)) {
                if (item.getName() == null && item.getDescription() == null) {
                    item1.setAvailable(item.getAvailable());
                    return item1;
                } else if (item.getName() == null && item.getAvailable() == null) {
                    item1.setDescription(item.getDescription());
                    return item1;
                } else if (item.getDescription() == null && item.getAvailable() == null) {
                    item1.setName(item.getName());
                    return item1;
                } else {
                    item1.setName(item.getName());
                    item1.setDescription(item.getDescription());
                    item1.setAvailable(item.getAvailable());
                    return item;
                }
            }
        }
        return null;
    }

    @Override
    public List<Item> get(Long userId) {
        List<Item> result = new ArrayList<>();
        for (Item item : userItems.values()) {
            if (item.getOwner().equals(userId)) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public Item getById(Long userId, Long id) {
        return userItems.get(id);
    }

    @Override
    public TreeSet<Item> search(Long userId, String text) {
        TreeSet<Item> result = new TreeSet<>(Comparator.comparing(Item::getId));
        if (text.length() <= 0) {
            return result;
        }
        for (Item item : userItems.values()) {
            if (!item.isAvailable()) {
                continue;
            } else {
                List<String> names = List.of(item.getName().split(" "));
                for (String name : names) {
                    if (name.toLowerCase().contains(text.toLowerCase())) {
                        result.add(item);
                    }
                }
                List<String> descriptions = List.of(item.getDescription().split(" "));
                for (String description : descriptions) {
                    if (description.toLowerCase().contains(text.toLowerCase())) {
                        result.add(item);
                    }
                }
            }
        }
        return result;
    }
}
