package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User getUser(Long userId) {
        return users.get(userId);
    }

    @Override
    public User createUser(User user) {
        log.trace("Добавляем пользователя");
        checkName(user);
        log.trace("Присваиваем id пользователю");
        user.setId(getNextId());
        log.trace("Добавляем пользователя в базу");
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        log.trace("Получаем список пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        log.trace("Обновляем пользователя");
        checkName(user);
        if (!(users.containsKey(user.getId()))) {
            log.error("Id пользователя не найден");
            throw new NotFoundException("Id пользователя не найден");
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
