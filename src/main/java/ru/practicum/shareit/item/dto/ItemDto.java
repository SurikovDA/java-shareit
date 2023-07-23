package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
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
    private Boolean available;
    private User owner;
    private ItemRequest request;
}
