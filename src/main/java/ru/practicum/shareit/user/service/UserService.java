package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    UserDto create(User user);

    UserDto update(Long id, User user);

    UserDto get(Long userId);

    void delete(Long userId);

    List<UserDto> getAll();
}
