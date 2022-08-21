package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.IItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.TreeSet;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final IItemService iItemService;

    public ItemController(IItemService iItemService) {
        this.iItemService = iItemService;
    }

    @PostMapping
    public Item create(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") Long id) {
        return iItemService.create(id, item);
    }

    @PatchMapping("/{id}")
    public Item update(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id, @RequestBody Item item) {
        return iItemService.update(userId, id, item);
    }

    @GetMapping("/{id}")
    public Item getByItemId(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return iItemService.getById(userId, id);
    }

    @GetMapping()
    public List<Item> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return iItemService.get(userId);
    }

    @GetMapping("/search")
    public TreeSet<Item> search(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        return iItemService.search(userId, text);
    }
}

