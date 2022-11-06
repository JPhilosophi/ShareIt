package ru.practicum.shareit.requests.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.BadRequestException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequestEntity;
import ru.practicum.shareit.requests.model.ItemRequestInputDto;
import ru.practicum.shareit.requests.model.ItemRequestOutputDto;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.UserEntity;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service("ItemRequestRepository")
public class ItemRequestImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public ItemRequestImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemRequestOutputDto createRequest(ItemRequestInputDto itemRequestInputDto, Long userId) {
        if (itemRequestInputDto.getDescription().isEmpty()) {
            throw new BadRequestException("description can't be null");
        }
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("can't finde user"));
        ItemRequestEntity requestEntity = new ItemRequestEntity();
        requestEntity.setCreated(LocalDateTime.now());
        requestEntity.setDescription(itemRequestInputDto.getDescription());
        requestEntity.setRequesterId(userEntity.getId());
        requestEntity = itemRequestRepository.save(requestEntity);
        ItemRequestOutputDto itemOutput = ItemRequestMapper.itemRequestOutputDto(requestEntity);
        return itemOutput;
    }

    @Override
    public ItemRequestOutputDto findItemRequestById(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("can't finde user"));
        ItemRequestEntity requestEntity = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("can't finde user"));
        ItemRequestOutputDto itemRequet = ItemRequestMapper.itemRequestOutputDto(requestEntity);
        List<ItemDto> itemDtos = ItemMapper.mapToListItemDto(itemRepository.findAllByRequestId(requestId));
        itemRequet.setItems(itemDtos);
        return itemRequet;
    }

    @Override
    public List<ItemRequestOutputDto> findAllItemRequest(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("can't finde user"));
        List<ItemRequestOutputDto> itemRequests = ItemRequestMapper
                .mapToListRequestDto(itemRequestRepository.findAllById(List.of(userId)));
        List<ItemDto> itemDtos = ItemMapper.mapToListItemDto(itemRepository.findAllByRequestId(userEntity.getId()));
        for (ItemRequestOutputDto request : itemRequests) {
            request.setItems(itemDtos);
        }
        return itemRequests;
    }

    @Override
    public List<ItemRequestOutputDto> findPage(Long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("can't finde user"));
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created"));

        List<ItemRequestOutputDto> itemRequests = ItemRequestMapper
                .mapToListRequestDto(itemRequestRepository.findAllByRequesterIdNot(userId, pageable));
        for (ItemRequestOutputDto request : itemRequests) {
            List<ItemDto> itemDtos = ItemMapper.mapToListItemDto(itemRepository.findAllByRequestId(request.getId()));
            request.setItems(itemDtos);
        }
        return itemRequests;
    }
}
