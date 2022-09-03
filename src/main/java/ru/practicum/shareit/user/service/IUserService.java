package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface IUserService {

    User create(User user);

    User update(Long id, User user);

    User get(Long id);

    List<User> getUsers();

    void delete(Long id);

    void userVerification(User user);
}
