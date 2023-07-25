package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(User user) {
        User createUser = userRepository.create(user);
        log.info("Пользователь с id '{}' добавлен в список", createUser.getId());
        return UserMapper.toUserDto(createUser);
    }

    @Override
    public UserDto update(Long id, User user) {
        if (userRepository.getUserById(id) != null) {
            User updateUser = userRepository.update(id, user);
            log.info("Пользователь с id '{}' обновлен", updateUser.getId());
            return UserMapper.toUserDto(updateUser);
        } else {
            log.info("EntityNotFoundException (Такого пользователя нет в списке)");
            throw new EntityNotFoundException("Такого пользователя не существует!");
        }
    }

    @Override
    public UserDto get(Long userId) {
        if (userRepository.getUserById(userId) != null) {
            return UserMapper.toUserDto(userRepository.getUserById(userId));
        } else {
            throw new EntityNotFoundException(String.format("Пользователя с id=%d нет в списке", userId));
        }
    }

    @Override
    public void delete(Long userId) {
        if (userRepository.getUserById(userId) != null) {
            userRepository.delete(userId);
        } else {
            throw new EntityNotFoundException(String.format("Пользователя с id=%d нет в списке", userId));
        }
    }

    @Override
    public List<UserDto> getAll() {
        List<User> userList = userRepository.readAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(UserMapper.toUserDto(user));
        }
        return userDtoList;
    }
}
