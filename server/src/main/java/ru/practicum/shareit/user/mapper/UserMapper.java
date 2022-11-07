package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserEntity;

public class UserMapper {
    public static UserDto mapToUserDto(UserEntity userEntity) {
        UserDto user = new UserDto();
        user.setId(userEntity.getId());
        user.setName(userEntity.getName());
        user.setEmail(userEntity.getEmail());
        return user;
    }

    public static UserEntity mapToUserEntity(UserDto userDto) {
        UserEntity user = new UserEntity();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
