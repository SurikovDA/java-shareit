package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exeptions.EmailException;
import ru.practicum.shareit.exeptions.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users;
    private static long userId;

    public UserRepositoryImpl() {
        users = new HashMap<>();
    }

    private long generateId() {
        return ++userId;
    }

    @Override
    public User create(User user) {
        checkEmail(user, user.getId());
        user.setId(generateId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(Long id, User user) {
        checkUser(id);
        checkEmail(user, id);
        if (user.getName() != null) {
            users.get(id).setName(user.getName());
        }
        if (user.getEmail() != null) {
            users.get(id).setEmail(user.getEmail());
        }
        return users.get(id);
    }

    @Override
    public User getUserById(Long id) {
        checkUser(id);
        return users.get(id);
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    private void checkUser(Long userId) {
        if (!users.containsKey(userId)) {
            throw new EntityNotFoundException("Такого пользователя не существует!");
        }
    }

    private void checkEmail(User user, Long id) {
        for (User user1 : users.values()) {
            if ((user1.getEmail().equals(user.getEmail()))
                    && (!user1.getId().equals(id))) {
                throw new EmailException("EmailException (Пользователь с таким email уже существует!)");
            }
        }
    }
}
