package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;


@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userService;

    //Получение всех пользователей
    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Получен запрос GET /users");
        return userService.findAllUsers();
    }

    //Получение пользователя по id
    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id) {
        log.info("Получен запрос GET /users/{}", id);
        return userService.getUserById(id);
    }

    //Создание пользователя
    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос POST /users");
        return userService.createUser(userDto);
    }

    //Редактирование пользователя
    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody UserDto userDto) {
        log.info("Получен запрос PATCH /users");
        return userService.updateUser(userDto, id);
    }

    //Удаление пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("Получен запрос DELETE /users");
        return userService.deleteUser(id);
    }
}