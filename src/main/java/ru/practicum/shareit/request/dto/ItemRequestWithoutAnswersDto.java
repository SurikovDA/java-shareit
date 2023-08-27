package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestWithoutAnswersDto {
    private Long id;
    @NotBlank(message = "Описание не может быть пустым.")
    private String description;
    private User requestor;
    private LocalDateTime created;
}
