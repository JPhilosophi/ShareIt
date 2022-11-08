package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestInputDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient requestClient;

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
