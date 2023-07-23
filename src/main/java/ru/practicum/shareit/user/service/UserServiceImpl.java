package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.EmailException;
import ru.practicum.shareit.exeptions.EntityNotFoundException;
import ru.practicum.shareit.exeptions.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        checkEmail(user, user.getId());
        if (userRepository.getUserById(user.getId()) != null) {
            throw new ValidationException("Пользователь с таким id уже есть в базе");
        }
        User createUser = userRepository.create(user);
        log.info("Пользователь с id '{}' добавлен в список", createUser.getId());
        return createUser;
    }

    @Override
    public User update(Long id, User user) {
        checkEmail(user, id);
        if (userRepository.getUserById(id) != null) {
            User updateUser = userRepository.update(id, user);
            log.info("Пользователь с id '{}' обновлен", updateUser.getId());
            return updateUser;
        } else {
            log.info("EntityNotFoundException (Такого пользователя нет в списке)");
            throw new EntityNotFoundException("Такого пользователя не существует!");
        }
    }

    @Override
    public User get(Long userId) {
        if (userRepository.getUserById(userId) != null) {
            return userRepository.getUserById(userId);
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
    public List<User> getAll() {
        return userRepository.readAll();
    }


    public void checkEmail(User user, Long id) {
        for (User user1 : getAll()) {
            if ((user1.getEmail().equals(user.getEmail()))
                    && (!user1.getId().equals(id))) {
                throw new EmailException("EmailException (Пользователь с таким email уже существует!)");
            }
        }
    }
}
