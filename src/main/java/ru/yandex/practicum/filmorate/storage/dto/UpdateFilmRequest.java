package ru.yandex.practicum.filmorate.storage.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.customAnnotation.MinReleaseDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateFilmRequest {
    @NotNull
    private Long id;
    @NotNull
    private String name;
    @Size(max = 200)
    private String description;
    @MinReleaseDate
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;
    @Valid
    private List<GenreDto> genres = new ArrayList<>();
    @Valid
    private MpaDto mpa;
}
