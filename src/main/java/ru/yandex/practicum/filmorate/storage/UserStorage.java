package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Collectors;

public interface UserStorage {

    public List<Long> getIds();

    public User getUser(Long userId);

    public void addOrUpdateUser(User user);

    public List<User> getUsers();

    public List<User> getCommonFriends(long userId, long friendId);

    public List<User> getFriends(long userId);
}