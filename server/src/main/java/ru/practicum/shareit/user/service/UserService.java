package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto user);

    UserDto update(Long id, UserDto user);

    UserDto get(Long id);

    List<UserDto> getUsers();

    void delete(Long id);
}
