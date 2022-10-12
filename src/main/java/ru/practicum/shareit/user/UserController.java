package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService UserService;

    public UserController(@Qualifier("UserRepository") UserService userService) {
        UserService = userService;
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto user) {
        return UserService.create(user);
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto user, @PathVariable Long id) {
        return UserService.update(id, user);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return UserService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        return UserService.get(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        UserService.delete(id);
    }
}
