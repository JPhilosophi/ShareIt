package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.BookingEntity;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemEntity;
import ru.practicum.shareit.user.model.UserEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ItemMapperTest {
    @InjectMocks
    private ItemEntity item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity();
        user.setId(1L);

        UserEntity request = new UserEntity();
        request.setId(2L);

        BookingEntity last = new BookingEntity();
        last.setId(2L);

        BookingEntity next = new BookingEntity();
        next.setId(1L);

        item = new ItemEntity();
        item.setId(2L);
        item.setName("Санки");
        item.setDescription("Санки");
        item.setOwnerId(1L);
        item.setRequestId(2L);
        item.setAvailable(true);
        item.setNextBookingId(1L);
        item.setLastBookingId(2L);

        itemDto = new ItemDto();
        itemDto.setId(2L);
        itemDto.setName("Санки");
        itemDto.setDescription("Санки");
        itemDto.setOwner(1L);
        itemDto.setRequestId(2L);
        itemDto.setAvailable(true);
    }

    @Test
    void mapToItemDto() {
        ItemDto testItem = ItemMapper.mapToItemDto(item);
        assertEquals(testItem.getName(), item.getName());
        assertEquals(testItem.getId(), item.getId());
    }

    @Test
    void toBookingDto() {
        ItemEntity itemEntity = ItemMapper.mapToItemEntity(itemDto);
        assertEquals(itemEntity.getId(), itemDto.getId());
        assertEquals(itemEntity.getName(), itemDto.getName());
    }

    @Test
    void mapToListItemDto() {
        List<ItemDto> result = ItemMapper.mapToListItemDto(List.of(item));
        assertEquals(result.size(), 1);
        assertNotNull(result);
    }
}
