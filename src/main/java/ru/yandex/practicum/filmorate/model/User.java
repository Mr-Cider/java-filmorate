package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.customAnnotation.CreateValidation;
import ru.yandex.practicum.filmorate.customAnnotation.NoSpace;
import ru.yandex.practicum.filmorate.customAnnotation.UpdateValidation;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotNull(groups = UpdateValidation.class, message = "Нужно ввести id")
    private Long id;
    @NotNull(groups = CreateValidation.class, message = "Email не может быть пустым")
    @Email(groups = {CreateValidation.class, UpdateValidation.class}, message = "Некорректный формат Email")
    private String email;
    @NotBlank(groups = CreateValidation.class, message = "Логин не может быть пустым")
    @NoSpace(groups = {CreateValidation.class, UpdateValidation.class}, message = "Поле не должно содержать пробелов")
    private String login;
    private String name;
    @PastOrPresent(groups = {CreateValidation.class, UpdateValidation.class}, message = "Дата рождения не может быть раньше текущей")
    private LocalDate birthday;
}
