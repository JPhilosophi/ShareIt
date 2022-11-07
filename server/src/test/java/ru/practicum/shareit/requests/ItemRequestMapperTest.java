package ru.practicum.shareit.requests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequestEntity;
import ru.practicum.shareit.requests.model.ItemRequestOutputDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ItemRequestMapperTest {
    @InjectMocks
    private ItemRequestEntity itemRequest;
    private ItemRequestOutputDto itemRequestOutputDto;

    @BeforeEach
    void setUp() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setOwner(1L);
        itemDto.setRequestId(1L);

        itemRequest = new ItemRequestEntity();
        itemRequest.setId(1L);
        itemRequest.setDescription("test");
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequesterId(1L);

        itemRequestOutputDto = new ItemRequestOutputDto();
        itemRequestOutputDto.setId(1L);
        itemRequestOutputDto.setDescription("test");
        itemRequestOutputDto.setCreated(LocalDateTime.now());
        itemRequestOutputDto.setItems(List.of(itemDto));
    }


    @Test
    void toItemRequestEntity() {
        ItemRequestEntity test = ItemRequestMapper.toItemRequestEntity(itemRequestOutputDto, 1L);
        assertEquals(itemRequestOutputDto.getId(), test.getId());
    }

    @Test
    void itemRequestOutputDto() {
        ItemRequestOutputDto test = ItemRequestMapper.itemRequestOutputDto(itemRequest);
        assertEquals(itemRequest.getId(), test.getId());
    }

    @Test
    void mapToListRequestDto() {
        List<ItemRequestOutputDto> result = ItemRequestMapper.mapToListRequestDto(List.of(itemRequest));
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }
}
