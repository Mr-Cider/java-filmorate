package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User getUser(Long userId) {
        return users.get(userId);
    }

    @Override
    public void addOrUpdateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<Long> getIds() {
        return new ArrayList<>(users.keySet());
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        List<User> userFriends = getFriends(userId);
        List<User> friendsFriends = getFriends(friendId);
        return userFriends.stream()
                .filter(friendsFriends::contains)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getFriends(long userId) {
        User user = checkUser(userId);
        return user.getFriendsIds().stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    private User checkUser(long userId) {
        User user = users.get(userId);
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + "не найден.");
        }
        return user;
    }
}
