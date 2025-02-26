package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public User createFilm(@Valid @RequestBody User user, BindingResult bindingResult) {
        log.trace("Добавляем пользователя");
        Checkers.checkErrorValidation(bindingResult, log);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Пользователь не ввёл имя. Имя = Логин");
        }
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
    public User updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        log.trace("Обновляем пользователя");
        if (!(users.containsKey(user.getId()))) {
            log.error("Id пользователя не найден");
            throw new ValidationException(HttpStatus.NOT_FOUND);
        }
        Checkers.checkErrorValidation(bindingResult, log);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("Пользователь не ввёл имя. Имя = Логин");
        }
        log.trace("Обновляем пользователя в базе");
        users.put(user.getId(), user);
        return user;
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
