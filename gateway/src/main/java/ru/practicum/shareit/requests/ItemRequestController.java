package ru.practicum.shareit.requests;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestInputDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    public ItemRequestController(ItemRequestClient requestService) {
        this.requestClient = requestService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody ItemRequestInputDto itemInput) {
        return requestClient.createRequest(userId, itemInput);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                              @PathVariable Long requestId) {
        return requestClient.findItemRequestById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestAll(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return requestClient.getRequestAll(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestsPage(@RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(defaultValue = "10") Integer size,
                                                  @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return requestClient.getRequestsPage(userId, from, size);
    }

}
