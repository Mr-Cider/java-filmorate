package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.customAnnotation.CreateValidation;
import ru.yandex.practicum.filmorate.customAnnotation.UpdateValidation;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private static final LocalDate REALESE_FIRST_FILM = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@Validated(CreateValidation.class) @RequestBody Film film, BindingResult bindingResult) {
        Checkers.checkErrorValidation(bindingResult, log);
        return filmStorage.createFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @PutMapping
    public Film updateFilm(@Validated(UpdateValidation.class) @RequestBody Film film, BindingResult bindingResult) {
        Checkers.checkErrorValidation(bindingResult, log);
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public long likeFilm(@PathVariable long userId, @PathVariable long filmId) {
        return filmService.giveLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public long deleteLikeFilm(@PathVariable long userId, @PathVariable long filmId) {
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTop10Films(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTop10Films(count);
    }
}
