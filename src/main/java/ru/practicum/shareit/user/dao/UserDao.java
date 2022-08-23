package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.BadRequestException;
import ru.practicum.shareit.error.DuplicateError;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("memoryUser")
public class UserDao implements IUserDao {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        checkDuplicate(user);
        user.getNextId();
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User update(Long id, User user) {
        if (!users.containsKey(id)) {
            throw new BadRequestException("Can't find user with " + id);
        }
        if (user.getEmail() == null) {
            users.get(id).setName(user.getName());
        } else if (user.getName() == null) {
            checkDuplicate(user);
            users.get(id).setEmail(user.getEmail());
        } else {
            checkDuplicate(user);
            users.get(id).setName(user.getName());
            users.get(id).setEmail(user.getEmail());
        }
        return users.get(id);
    }

    @Override
    public User get(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> getUsers() {
        List<User> userList = users.values().stream().collect(Collectors.toList());
        return userList;
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public Map<Long, User> checkUserInStorage() {
        return users;
    }

    private void checkDuplicate(User user) {
        if (users.values().stream().anyMatch(user1 -> user.getEmail().equals(user1.getEmail()))) {
            throw new DuplicateError("Email mast by unique");
        }
    }
}
