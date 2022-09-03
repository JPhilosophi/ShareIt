package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;

public interface IUserDao {

    User create(User user);

    User update(Long id, User user);

    User get(Long id);

    List<User> getUsers();

    Map<Long, User> checkUserInStorage();

    void delete(Long id);
}
