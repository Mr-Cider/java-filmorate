package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User createUser(User user) {
        checkName(user);
        user.setId(getNextId());
        userStorage.addOrUpdateUser(user);
        return user;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User updateUser(User user) {
        checkName(user);
        if (userStorage.getUser(user.getId()) == null) {
            throw new NotFoundException("Id пользователя не найден");
        }
        userStorage.addOrUpdateUser(user);
        return user;
    }

    public void addFriend(long userId, long friendId) {
        User user = checkUser(userId);
        User friend = checkUser(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(long userId, long friendId) {
        User user = checkUser(userId);
        User friend = checkUser(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public List<User> getFriends(long userId) {
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

    private User checkUser(long userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + "не найден.");
        }
        return user;
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private long getNextId() {
        long currentMaxId = userStorage.getIds()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
