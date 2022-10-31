package ru.practicum.shareit.requests.mapper;

import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.requests.model.ItemRequestEntity;
import ru.practicum.shareit.requests.model.ItemRequestOutputDto;

import java.util.ArrayList;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequestEntity toItemRequestEntity(ItemRequestOutputDto requestOutput, Long userId) {
        ItemRequestEntity itemRequest = new ItemRequestEntity();
        itemRequest.setRequesterId(userId);
        itemRequest.setDescription(requestOutput.getDescription());
        itemRequest.setCreated(requestOutput.getCreated());
        return itemRequest;
    }

    public static ItemRequestOutputDto itemRequestOutputDto (ItemRequestEntity itemRequestEntity) {
        ItemRequestOutputDto itemRequest = new ItemRequestOutputDto();
        itemRequest.setId(itemRequestEntity.getId());
        itemRequest.setCreated(itemRequestEntity.getCreated());
        itemRequest.setDescription(itemRequestEntity.getDescription());
        return itemRequest;
    }

    public static List<ItemRequestOutputDto> mapToListRequestDto(Iterable<ItemRequestEntity> itemEntities) {
        List<ItemRequestOutputDto> result = new ArrayList<>();

        for (ItemRequestEntity item : itemEntities) {
            result.add(itemRequestOutputDto(item));
        }
        return result;
    }
}
