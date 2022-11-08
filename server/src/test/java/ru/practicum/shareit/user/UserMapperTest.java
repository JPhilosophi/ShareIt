package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {
    @InjectMocks
    private UserEntity userEntity;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("name");
        userDto.setEmail("email@email.com");

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("name");
        userEntity.setEmail("email@email.com");
    }

    @Test
    void mapToUserDto() {
        UserDto userDto1 = UserMapper.mapToUserDto(userEntity);
        assertEquals(userDto1.getName(), userDto.getName());
        assertEquals(userDto1.getId(), userDto.getId());
    }

    @Test
    void mapToUserEntity() {
        UserEntity userEntity1 = UserMapper.mapToUserEntity(userDto);
        assertEquals(userEntity1.getName(), userEntity.getName());
        assertEquals(userEntity1.getId(), userEntity.getId());
    }
}
