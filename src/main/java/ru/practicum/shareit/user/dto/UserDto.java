package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    @Positive(message = "id не может быть меньше нуля!")
    private Long id;
    @NotBlank(message = "name не может быть пустым!")
    private String name;
    @Email(message = "Не верно введен email!")
    @NotBlank(message = "email не может быть пустым!")
    private String email;
}
