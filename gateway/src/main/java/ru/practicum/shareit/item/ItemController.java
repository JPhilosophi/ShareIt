package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@RequestMapping(path = "/items")
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }


    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemDto item, @RequestHeader("X-Sharer-User-Id") Long id) {
        return itemClient.create(id, item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Valid @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id, @RequestBody ItemDto item) {
        return itemClient.update(userId, id, item);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getByItemId(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return itemClient.getByItemId(userId, id);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/search") //sort -> поля берутчя из объекта.
    public ResponseEntity<Object> search(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        return itemClient.search(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                @PathVariable @NotNull Long itemId,
                                                @Valid @RequestBody CommentDto comment) {
        return itemClient.createComment(userId, itemId, comment);
    }
}
