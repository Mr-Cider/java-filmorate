package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    private final Map<Long, Map<Long, Boolean>> friends = new HashMap<>();

    @Override
    public Optional<User> getUser(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        friends.computeIfAbsent(userId, k -> new HashMap<>())
                .put(friendId, false);
    }

    @Override
    public void acceptFriend(Long userId, Long friendId) {
        friends.computeIfAbsent(userId, k -> new HashMap<>())
                .put(friendId, true);
        }

    @Override
    public void removeFriend(Long userId, Long friendId) {
            friends.get(userId).remove(friendId);
        }


    @Override
    public List<User> getFriendsRequest(Long userId) {
        return friends.getOrDefault(userId, Collections.emptyMap())
                .entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .map(users::get).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
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
        return friends.getOrDefault(userId, Collections.emptyMap())
                .entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(users::get).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Long generateId() {
        return getIds()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
    }

    private User checkUser(long userId) {
        User user = users.get(userId);
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + "не найден.");
        }
        return user;
    }


}
