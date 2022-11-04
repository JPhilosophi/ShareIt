package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.ShortUserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortUserDtoTest {
    @Test
    void ShortUserDto() {
        ShortUserDto shortUserDto = new ShortUserDto(1L);
        assertEquals(shortUserDto.getId(), 1L);
    }
}
