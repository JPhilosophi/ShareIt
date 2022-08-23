package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.IUserDao;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@Service
public class UserService implements IUserService {
    private final IUserDao userStorage;

    public UserService(@Qualifier("memoryUser") IUserDao userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(Long id, User user) {
        return userStorage.update(id, user);
    }

    @Override
    public User get(Long id) {
        return userStorage.get(id);
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public void delete(Long id) {
        userStorage.delete(id);
    }

    @Override
    public void userVerification(User user) {

    }
}
