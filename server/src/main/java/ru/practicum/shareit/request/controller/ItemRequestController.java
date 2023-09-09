package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.ItemRequestWithoutAnswersDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequest create(@RequestHeader("X-Sharer-User-id") long userId,
                              @RequestBody ItemRequestWithoutAnswersDto itemRequestDto) {
        log.info("Получен запрос POST /requests");
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestWithAnswersDto> getAllByRequestorId(@RequestHeader("X-Sharer-User-id") long userId,
                                                               @RequestParam(defaultValue = "0") int from,
                                                               @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /requests");
        return itemRequestService.getAllByRequestorId(userId, from, size);
    }

    @GetMapping("/all")
    public List<ItemRequestWithAnswersDto> getAll(@RequestHeader("X-Sharer-User-id") long userId,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /requests/all");
        return itemRequestService.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithAnswersDto getById(@RequestHeader("X-Sharer-User-id") long userId,
                                             @PathVariable Long requestId) {
        log.info("Получен запрос GET /requests/{}", requestId);
        return itemRequestService.getById(userId, requestId);
    }
}
