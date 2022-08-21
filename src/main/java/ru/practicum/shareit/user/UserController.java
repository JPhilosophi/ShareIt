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
    private final IUserService IUserService;

    @Autowired
    public UserController(IUserService iUserService) {
        IUserService = iUserService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return IUserService.create(user);
    }

    @PatchMapping("/{id}")
    public User update(@RequestBody User user, @PathVariable Long id) {
        return IUserService.update(id, user);
    }

    @GetMapping
    public List<User> getAll() {
        return IUserService.getUsers();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return IUserService.get(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {IUserService.delete(id);}

}
