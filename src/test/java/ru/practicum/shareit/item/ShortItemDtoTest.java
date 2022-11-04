package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.ShortItemDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortItemDtoTest {
    @Test
    void shortItemDto() {
        ShortItemDto shortItemDto = new ShortItemDto(1L, "name");
        assertEquals(shortItemDto.getId(), 1L);
        assertEquals(shortItemDto.getName(), "name");
    }
}
