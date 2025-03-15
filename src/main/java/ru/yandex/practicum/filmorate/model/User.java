package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.customAnnotation.CreateValidation;
import ru.yandex.practicum.filmorate.customAnnotation.NoSpace;
import ru.yandex.practicum.filmorate.customAnnotation.UpdateValidation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @NotNull(groups = UpdateValidation.class, message = "Нужно ввести id")
    private long id;
    private Set<Long> friendsIds = new HashSet<>();
    @NotNull(groups = CreateValidation.class, message = "Email не может быть пустым")
    @Email(groups = {CreateValidation.class, UpdateValidation.class}, message = "Некорректный формат Email")
    private String email;
    @NotBlank(groups = CreateValidation.class, message = "Логин не может быть пустым")
    @NoSpace(groups = {CreateValidation.class, UpdateValidation.class}, message = "Поле не должно содержать пробелов")
    private String login;
    private String name;
    @PastOrPresent(groups = {CreateValidation.class, UpdateValidation.class}, message = "Дата рождения не может быть раньше текущей")
    private LocalDate birthday;

    public void addFriend(Long id) {
        friendsIds.add(id);
    }

    public void removeFriend(Long id) {
        friendsIds.remove(id);
    }
}
