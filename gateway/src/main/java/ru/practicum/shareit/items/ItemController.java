package ru.practicum.shareit.items;

import ru.practicum.shareit.items.dto.CommentDto;
import ru.practicum.shareit.items.dto.ItemDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    public ItemController( ItemClient itemClient) {
        this.itemClient = itemClient;
    }


    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") Long id) {
        return itemClient.create(id, item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long id, @RequestBody ItemDto item) {
        return itemClient.update(userId, id, item);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getByItemId(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return itemClient.getById(userId, id);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllItems(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                              @PositiveOrZero @RequestParam(name = "from",
                                             defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size",
                                             defaultValue = "10") Integer size) {
        return itemClient.getAll(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        return itemClient.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto comment,
                                                @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                @PathVariable Long itemId) {
        return itemClient.createComment(comment, userId, itemId);
    }

}
