package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestWithoutAnswersDto {
    private Long id;
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
}
