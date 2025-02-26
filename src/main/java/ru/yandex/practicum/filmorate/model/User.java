package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import ru.yandex.practicum.filmorate.service.NoSpace;

import java.time.LocalDate;

@Data
public class User {
    private long id;
    @NotNull(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат Email")
    private String email;
    @NotNull(message = "Логин не может быть пустым")
    @NotBlank(message = "Логин не может быть пустым")
    @NoSpace(message = "Поле не должно содержать пробелов")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть раньше текущей")
    private LocalDate birthday;
}
