package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.RequestMapper;

import java.util.List;

public class ItemMapper {

    //Из item в ItemDto
    public static ItemDto toItemDto(Item item) {
        return ItemDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(RequestMapper.toRequestDto(item.getRequest()))
                .build();
    }

    //Из itemDto в item
    public static Item toItem(ItemDto itemDto) {
        return Item
                .builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .request(RequestMapper.toRequest(itemDto.getRequest()))
                .build();
    }

    public static ItemBookingDto toItemWishBookingAndCommentDto(
            Item item, BookingItemDto lastBooking, BookingItemDto nextBooking, List<CommentDto> comments) {

        return new ItemBookingDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBooking,
                nextBooking,
                comments
        );
    }
}
