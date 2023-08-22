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
        User createUser = userRepository.save(user);
        log.info("Пользователь с id '{}' добавлен в список", createUser.getId());
        return UserMapper.toUserDto(createUser);
    }

    @Override
    public UserDto update(Long id, User user) {
        if (userRepository.findById(id).isPresent()) {
            if (user.getName() != null) {
                userRepository.findById(id).get().setName(user.getName());
            }
            if (user.getEmail() != null) {
                userRepository.findById(id).get().setEmail(user.getEmail());
            }
            User updateUser = userRepository.save(userRepository.findById(id).get());
            log.info("Пользователь с id '{}' обновлен", updateUser.getId());
            return UserMapper.toUserDto(updateUser);
        } else {
            log.info("EntityNotFoundException (Такого пользователя нет в списке)");
            throw new EntityNotFoundException("Такого пользователя не существует!");
        }
    }

    @Override
    public UserDto get(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            return UserMapper.toUserDto(userRepository.findById(userId).get());
        } else {
            throw new EntityNotFoundException(String.format("Пользователя с id=%d нет в списке", userId));
        }
    }

    @Override
    public void delete(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
        } else {
            throw new EntityNotFoundException(String.format("Пользователя с id=%d нет в списке", userId));
        }
    }

    @Override
    public List<UserDto> getAll() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(UserMapper.toUserDto(user));
        }
        return userDtoList;
    }
}
