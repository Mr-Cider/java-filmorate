package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public interface UserStorage {

    Long generateId();

    List<Long> getIds();

    Optional<User> getUser(Long userId);

    User addUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    public void addFriend(Long userId, Long friendId);

    public void acceptFriend(Long userId, Long friendId);

    public void removeFriend(Long userId, Long friendId);

    public List<User>   getFriendsRequest(Long userId);

    List<User> getCommonFriends(long userId, long friendId);

    List<User> getFriends(long userId);
}