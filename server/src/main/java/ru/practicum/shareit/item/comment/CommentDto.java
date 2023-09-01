package ru.practicum.shareit.item.comment;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    @NotBlank(message = "Комментарий не может быть пустым")
    private String text;
    private ItemDto item;
    private Long authorId;
    private String authorName;
    private LocalDateTime created;
}
