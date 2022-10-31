package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemEntity;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static ItemDto mapToItemDto(ItemEntity itemEntity) {
        ItemDto item = new ItemDto();
        item.setId(itemEntity.getId());
        item.setName(itemEntity.getName());
        item.setDescription(itemEntity.getDescription());
        item.setOwner(itemEntity.getOwnerId());
        item.setAvailable(itemEntity.getAvailable());
        item.setRequestId(itemEntity.getRequestId());
        return item;
    }

    public static ItemEntity mapToItemEntity(ItemDto item) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(item.getId());
        itemEntity.setName(item.getName());
        itemEntity.setDescription(item.getDescription());
        itemEntity.setOwnerId(item.getOwner());
        itemEntity.setAvailable(item.getAvailable());
        itemEntity.setRequestId(item.getRequestId());
        itemEntity.setLastBookingId(itemEntity.getLastBookingId());
        itemEntity.setNextBookingId(itemEntity.getNextBookingId());
        return itemEntity;
    }

    public static List<ItemDto> mapToListItemDto(Iterable<ItemEntity> itemEntities) {
        List<ItemDto> result = new ArrayList<>();

        for (ItemEntity item : itemEntities) {
            result.add(mapToItemDto(item));
        }
        return result;
    }
}
