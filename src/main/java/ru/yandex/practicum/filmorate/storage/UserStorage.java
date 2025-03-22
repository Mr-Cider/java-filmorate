package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<Long> getIds();

    User getUser(Long userId);

    void addOrUpdateUser(User user);

    List<User> getUsers();

    List<User> getCommonFriends(long userId, long friendId);

    List<User> getFriends(long userId);
}