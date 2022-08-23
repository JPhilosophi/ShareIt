package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserDto {

    public UserDto(String name, String email) {
        super();
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getName(),
                user.getEmail()
        );
    }
}
