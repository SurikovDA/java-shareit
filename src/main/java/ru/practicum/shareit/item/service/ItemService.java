package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(Item item, Long userId);

    List<ItemDto> readAll(Integer from, Integer size);

    List<ItemBookingDto> readAllByUserId(Long id, Integer from, Integer size);

    ItemDto update(Long id, Item item, Long userId);

    ItemDto getItemById(Long id);

    void delete(Long id, Long userId);

    List<ItemDto> findItemsByText(String text, Integer from, Integer size);

    public CommentDto createComment(Long itemId, Long userId, Comment comment);

    public ItemBookingDto getItemByUserId(Long id, Long userId);
}
