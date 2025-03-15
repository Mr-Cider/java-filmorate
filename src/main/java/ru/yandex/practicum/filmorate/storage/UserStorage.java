package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public User getUser(Long userId);

    public User createUser(User user);

    public List<User> getUsers();

    public User updateUser(User user);
}