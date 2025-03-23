package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import ru.yandex.practicum.filmorate.customAnnotation.CreateValidation;
import ru.yandex.practicum.filmorate.customAnnotation.UpdateValidation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    private static final int MAX_SIZE_OF_DESCRIPTION = 200;

    @NotNull(groups = UpdateValidation.class)
    private Long id;
    @NotBlank(groups = {CreateValidation.class, UpdateValidation.class}, message = "Названия фильма не может быть пустым")
    private String name;
    @Size(groups = {CreateValidation.class, UpdateValidation.class}, max = MAX_SIZE_OF_DESCRIPTION)
    private String description;
    private LocalDate releaseDate;
    @Positive(groups = {CreateValidation.class, UpdateValidation.class}, message = "Продолжительность должна быть положительным числом")
    private int duration;
    private Set<Long> listUsersLikeId = new HashSet<>();
    private Set<String> genre = new HashSet<>();
    private Set<String> rate = new HashSet<>();


    public void addUserLike(long userId) {
        listUsersLikeId.add(userId);
    }

    public void removeUserLike(long userId) {
        listUsersLikeId.remove(userId);
    }

    @Getter
    @AllArgsConstructor
    enum rateMPA {
        G("G"),
        PG("PG"),
        PG_13("PG-13"),
        R("R"),
        NC_17("NC-17");

        private final String rate;
    }
}



