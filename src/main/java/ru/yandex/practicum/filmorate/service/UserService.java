package ru.yandex.practicum.filmorate.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dto.NewUserRequest;
import ru.yandex.practicum.filmorate.storage.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.storage.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public UserDto createUser(NewUserRequest request) {
        User user = convertToUser(request);
        validateName(user);
        User createdUser = userStorage.addUser(user);
        return convertToDto(createdUser);
    }

    public List<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(UpdateUserRequest request) {
        User user = getUserOrThrow(request.getId());
        updateUserFields(user, request);
        User updatedUser = userStorage.updateUser(user);
        return convertToDto(updatedUser);
    }

    @Transactional
    public void addFriend(Long userId, Long friendId) {
        validateFriendship(userId, friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        validateUserExists(userId);
        validateUserExists(friendId);
        userStorage.removeFriend(userId, friendId);
    }

    public List<UserDto> getFriends(Long userId) {
        validateUserExists(userId);
        return userStorage.getFriends(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getCommonFriends(Long userId, Long friendId) {
        validateUserExists(userId);
        validateUserExists(friendId);
        return userStorage.getCommonFriends(userId, friendId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private User convertToUser(NewUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .login(request.getLogin())
                .name(request.getName())
                .birthday(request.getBirthday())
                .build();
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }

    private void validateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private User getUserOrThrow(Long userId) {
        return userStorage.getUser(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    private void updateUserFields(User user, UpdateUserRequest request) {
        user.setEmail(request.getEmail());
        user.setLogin(request.getLogin());
        user.setBirthday(request.getBirthday());
        user.setName(request.getName());
    }

    private void validateFriendship(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя добавить самого себя в друзья");
        }
        validateUserExists(userId);
        validateUserExists(friendId);
    }

    private void validateUserExists(Long userId) {
        if (userStorage.getUser(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
    }
}