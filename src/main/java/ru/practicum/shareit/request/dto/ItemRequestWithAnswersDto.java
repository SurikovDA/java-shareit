package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestWithAnswersDto {

    private Long id;
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
    private List<ItemDto> items;
}
