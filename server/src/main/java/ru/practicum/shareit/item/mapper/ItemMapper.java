package ru.practicum.shareit.item.mapper;

import lombok.Generated;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Generated
public class ItemMapper {

    //Из item в ItemDto
    public static ItemDto toItemDto(Item item) {
        return ItemDto
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getItemRequest() == null ? null : item.getItemRequest().getId())
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
                .itemRequest(itemDto.getRequestId() == null ? null : ItemRequest.builder()
                        .id(itemDto.getRequestId())
                        .build())
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
