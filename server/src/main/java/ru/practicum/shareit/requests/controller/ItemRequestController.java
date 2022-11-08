package ru.practicum.shareit.requests.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.model.ItemRequestInputDto;
import ru.practicum.shareit.requests.model.ItemRequestOutputDto;
import ru.practicum.shareit.requests.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    public ItemRequestController(ItemRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ItemRequestOutputDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @Valid @RequestBody ItemRequestInputDto itemInput) {
        return requestService.createRequest(itemInput, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestOutputDto findRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                            @PathVariable Long requestId) {
        return requestService.findItemRequestById(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestOutputDto> findAll(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return requestService.findAllItemRequest(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestOutputDto> findPageOfThisSize(@RequestParam(defaultValue = "0") Integer from,
                                                         @RequestParam(defaultValue = "10") Integer size,
                                                         @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return requestService.findPage(userId, from, size);
    }

}
