package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

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
        User user = checkUser(userId);
        return user.getFriendsIds().stream()
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        List<User> userFriends = getFriends(userId);
        List<User> friendsFriends = getFriends(friendId);
        return userFriends.stream()
                .filter(friendsFriends::contains)
                .collect(Collectors.toList());
    }

    private User checkUser(long userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + "не найден.");
        }
        return user;
    }
}
