package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserStorage {

    List<Long> getIds();

    Optional<User> getUser(Long userId);

    User addUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    void addFriend(Long userId, Long friendId);

    void acceptFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<User> getFriendsRequest(Long userId);

    List<User> getCommonFriends(long userId, long friendId);

    List<User> getFriends(long userId);
}