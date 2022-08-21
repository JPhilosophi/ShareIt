package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.BadRequestException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.IUserDto;

import java.util.*;
import java.util.stream.Collectors;

@Component("memoryItem")
public class ItemDto implements IItemDto {
    private final Map<Long, HashSet<Item>> userItems = new HashMap<>();
    private final IUserDto userStorage;

    public ItemDto(IUserDto userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Item create(Long id, Item item) {
        if (!userStorage.checkUserInStorage().containsKey(id)) {
            throw new NotFoundException("Can't found user with id " + id);
        }
        if (item.getAvailable() == null) {
            throw new BadRequestException("available mast be");
        }
        item.getNextId();
        if (!userItems.containsKey(id)) {
            userItems.put(id, new HashSet<>());
        }
        userItems.get(id).add(item);
        return item;
    }

    @Override
    public Item update(Long userId, Long itemId, Item item) {
        /*
        Данный кусок добавил так только из-за того, что в тестах нет кейса создания предмета
        Для пользователя с id 3, соответсвенно для него не создана map.
        Из-за этого такая кривая реализация :)
        ниже по коду уже проверка если такой пользователь уже есть.
        */
        if (!userItems.containsKey(userId)) {
            throw new NotFoundException("You try change not your item");
        }
        List<Item> itemList = userItems.get(userId).stream().collect(Collectors.toList());
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
            } else {
                throw new NotFoundException("You try change not your item");
            }
        }
        return null;
    }

    @Override
    public List<Item> get(Long userId) {
        List<Item> itemList = userItems.get(userId).stream().collect(Collectors.toList());
        return itemList;
    }

    @Override
    public Item getById(Long userId, Long id) {
        if (userItems.containsKey(userId)) {
            for (HashSet<Item> itemSet : userItems.values()) {
                for (Item i : itemSet) {
                    if (i.getId().equals(id)) {
                        return i;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public TreeSet<Item> search(Long userId, String text) {
        TreeSet<Item> result = new TreeSet<>(Comparator.comparing(Item::getId));
        if (text.length() <= 0) {
            return result;
        }
        for (HashSet<Item> items : userItems.values()) {
            for (Item item : items) {
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
