package ru.yandex.practicum.filmorate.storage.dto;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.customAnnotation.MinReleaseDate;

import java.time.LocalDate;

@Data
public class NewFilmRequest {
    @NotBlank
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
