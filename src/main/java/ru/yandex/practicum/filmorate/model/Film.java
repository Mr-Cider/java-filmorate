package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.service.CreateValidation;
import ru.yandex.practicum.filmorate.service.UpdateValidation;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private final int maxSizeOfDescription = 200;

    @NotNull(groups = UpdateValidation.class)
    private long id;
    @NotBlank(groups = {CreateValidation.class, UpdateValidation.class}, message = "Названия фильма не может быть пустым")
    private String name;
    @Size(groups = {CreateValidation.class, UpdateValidation.class}, max = maxSizeOfDescription)
    private String description;
    private LocalDate releaseDate;
    @Positive(groups = {CreateValidation.class, UpdateValidation.class}, message = "Продолжительность должна быть положительным числом")
    private int duration;
}
