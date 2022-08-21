package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final IUserService userService;

    @Autowired
    public UserController(IUserService iUserService) {
        userService = iUserService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PatchMapping("/{id}")
    public User update(@RequestBody User user, @PathVariable Long id) {
        return userService.update(id, user);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.get(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }
}
