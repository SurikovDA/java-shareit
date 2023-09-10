package ru.practicum.shareit.user.dto;

import lombok.*;

@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
