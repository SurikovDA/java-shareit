package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
    private long itemId;
    @NotNull(message = "Дата старта не может быть пустой")
    @FutureOrPresent
    private LocalDateTime start;
    @NotNull(message = "Дата конца не может быть пустой")
    @FutureOrPresent
    private LocalDateTime end;
}
