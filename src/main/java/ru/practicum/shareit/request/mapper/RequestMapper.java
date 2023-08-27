package ru.practicum.shareit.request.mapper;

import lombok.Generated;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.ItemRequestWithoutAnswersDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.stream.Collectors;

@Generated
public class RequestMapper {

    public static ItemRequest toItemRequest(ItemRequestWithoutAnswersDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated())
                .requestor(itemRequestDto.getRequestor())
                .build();
    }

    public static ItemRequestWithAnswersDto toItemRequestWithAnswersDto(ItemRequest itemRequest) {
        return ItemRequestWithAnswersDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreated())
                .items(itemRequest.getItems().stream()
                        .map(ItemMapper::toItemDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
