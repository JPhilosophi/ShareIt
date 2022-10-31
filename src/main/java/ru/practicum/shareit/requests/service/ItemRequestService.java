package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.model.ItemRequestInputDto;
import ru.practicum.shareit.requests.model.ItemRequestOutputDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestOutputDto findItemRequestById(Long userId, Long requestId);

    ItemRequestOutputDto createRequest(ItemRequestInputDto inputDto, Long userId);

    List<ItemRequestOutputDto> findAllItemRequest(Long userId);

    List<ItemRequestOutputDto> findPage(Long userId, Integer from, Integer size);
}
