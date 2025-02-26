package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private final int maxSizeOfDescription = 200;

    private long id;
    @NotNull(message = "Названия фильма не может быть пустым")
    @NotBlank(message = "Названия фильма не может быть пустым")
    private String name;
    @Size(max = maxSizeOfDescription)
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительным числом")
    private int duration;
}
