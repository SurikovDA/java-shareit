package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ItemRequestDto {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
