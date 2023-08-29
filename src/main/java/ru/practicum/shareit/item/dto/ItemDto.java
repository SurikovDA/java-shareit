package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ItemDto {
    @Positive(message = "id не может быть меньше нуля!")
    private Long id;
    @NotBlank(message = "name не может быть пустым!")
    private String name;
    @NotBlank(message = "description не может быть пустым!")
    private String description;
    @NotNull(message = "available не может быть пустым!")
    private Boolean available;
    private UserDto owner;
    private ItemRequestDto request;
    private Long requestId;
}
