package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import ru.yandex.practicum.filmorate.customAnnotation.CreateValidation;
import ru.yandex.practicum.filmorate.customAnnotation.UpdateValidation;

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

    @NotNull(groups = UpdateValidation.class)
    private Long id;
    @NotBlank(groups = {CreateValidation.class, UpdateValidation.class}, message = "Названия фильма не может быть пустым")
    private String name;
    @Size(groups = {CreateValidation.class, UpdateValidation.class}, max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive(groups = {CreateValidation.class, UpdateValidation.class}, message = "Продолжительность должна быть положительным числом")
    private int duration;
    private List<Genre> genres;
    private Long mpa;
    @JsonIgnore
    private transient RateMPA rateMPA;

    @JsonIgnore
    public RateMPA getMpaRating() {
        return RateMPA.getById(this.mpa != null ? this.mpa : 1L);
    }

    @JsonProperty("mpa")
    public Map<String, Object> getMpaAsMap() {
        RateMPA rating = getMpaRating();
        return Map.of("id", rating.getId(), "name", rating.getDisplayName());
    }

    @JsonIgnore
    public Long getMpa() {
        return this.mpa;
    }
}




