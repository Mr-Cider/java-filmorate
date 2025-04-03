package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import ru.yandex.practicum.filmorate.customAnnotation.CreateValidation;
import ru.yandex.practicum.filmorate.customAnnotation.UpdateValidation;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.dto.GenreDto;

import java.time.LocalDate;
import java.util.*;

/**
 * Film.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private List<GenreDto> genres;
    private Long mpa;
    @JsonIgnore
    private transient RateMPA rateMPA;

    @JsonProperty("mpa")
    public Map<String, Object> getMpaForResponse() {
        if (this.mpa == null) {
            this.mpa = 1L;
        }
        RateMPA rating = RateMPA.getById(this.mpa);
        return Map.of(
                "id", this.mpa,
                "name", rating.getDisplayName()
        );
    }

    @JsonIgnore
    public Long getMpa() {
        return this.mpa;
    }

    @Getter
    public enum RateMPA {
        G(1, "G"),
        PG(2, "PG"),
        PG_13(3, "PG-13"),
        R(4, "R"),
        NC_17(5, "NC-17");

        private final long id;
        private final String name;

        RateMPA(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getDisplayName() {
            return name;
        }

        public static RateMPA getById(Long id) {
            return Arrays.stream(values())
                    .filter(rate -> rate.id == id)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("rate_id не найден"));
        }
    }
}




