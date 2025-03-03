package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.CreateValidation;
import ru.yandex.practicum.filmorate.service.UpdateValidation;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Validated(CreateValidation.class) @RequestBody User user, BindingResult bindingResult) {
        log.trace("Добавляем пользователя");
        Checkers.checkErrorValidation(bindingResult, log);
        checkName(user);
        log.trace("Присваиваем id пользователю");
        user.setId(getNextId());
        log.trace("Добавляем пользователя в базу");
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.trace("Получаем список пользователей");
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User updateUser(@Validated(UpdateValidation.class) @RequestBody User user, BindingResult bindingResult) {
        log.trace("Обновляем пользователя");
        Checkers.checkErrorValidation(bindingResult, log);
        checkName(user);
        if (!(users.containsKey(user.getId()))) {
            log.error("Id пользователя не найден");
            throw new ValidationException(HttpStatus.NOT_FOUND);
        }
        log.trace("Обновляем пользователя в базе");
        users.put(user.getId(), user);
        return user;
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Пользователь не ввёл имя. Имя = Логин");
        }
    }

    private long getNextId() {
        log.trace("Присвоение id");
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
