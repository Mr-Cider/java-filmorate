package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.customAnnotation.CreateValidation;
import ru.yandex.practicum.filmorate.customAnnotation.UpdateValidation;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@Validated(CreateValidation.class) @RequestBody User user, BindingResult bindingResult) {
        log.info("Создаем пользователя");
        Checkers.checkErrorValidation(bindingResult, log);
        log.debug("Пользователь создан");
        return userService.createUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PutMapping
    public User updateUser(@Validated(UpdateValidation.class) @RequestBody User user, BindingResult bindingResult) {
        log.info("Обновляем пользователя");
        Checkers.checkErrorValidation(bindingResult, log);
        log.debug("Пользователь обновлен");
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<User> addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.debug("Добавляем в друзья");
        userService.addFriend(id, friendId);
        log.info("Друг добавлен");
        return new ArrayList<>(userService.getFriends(id));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<User> removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.debug("Удаляем из друзей");
        userService.removeFriend(id, friendId);
        log.info("Друг удален");
        return new ArrayList<>(userService.getFriends(id));
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        log.debug("Получаем список друзей");
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.debug("Получаем список общих друзей");
        return userService.getCommonFriends(id, otherId);
    }
}
