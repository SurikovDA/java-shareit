package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    //Получение всех вещей по id пользователя
    @GetMapping
    public List<ItemBookingDto> getAll(@RequestHeader("X-Sharer-User-id") long userId,
                                       @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                       @Positive @RequestParam(defaultValue = "20") int size) {
        log.info("Получен запрос GET /items");
        return itemService.readAllByUserId(userId, from, size);
    }

    //Получение вещи по id пользователя
    @GetMapping("/{id}")
    public ItemBookingDto get(@RequestHeader("X-Sharer-User-id") Long userId, @Valid @PathVariable Long id) {
        log.info("Получен запрос GET /items/{}", id);
        return itemService.getItemByUserId(id, userId);
    }

    //Создание вещи
    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-id") long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос POST /items");
        Item item = ItemMapper.toItem(itemDto);
        return itemService.create(item, userId);
    }

    //Редактирование вещи
    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-id") long userId, @Valid @PathVariable Long id,
                          @RequestBody ItemDto itemDto) {
        log.info("Получен запрос PUT /items");
        Item item = ItemMapper.toItem(itemDto);
        return itemService.update(id, item, userId);
    }

    //Удаление вещи
    @DeleteMapping("/{id}")
    public void delete(@RequestHeader("X-Sharer-User-id") long userId, @Valid @PathVariable Long id) {
        log.info("Получен запрос DELETE /items");
        itemService.delete(id, userId);
    }

    //Поиск вещей
    @GetMapping("/search")
    private List<ItemDto> searching(@RequestParam String text,
                                    @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                    @Positive @RequestParam(defaultValue = "20") int size) {
        return itemService.findItemsByText(text, from, size);
    }

    //Добавление комментов
    @PostMapping("/{id}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-id") long userId, @Valid @PathVariable Long id,
                                 @Valid @RequestBody CommentDto commentDto) {
        log.info("Получен запрос POST /{}/comment", id);
        commentDto.setItem(itemService.getItemById(id));
        Comment comment = CommentMapper.toComment(commentDto);
        return itemService.createComment(id, userId, comment);
    }
}

