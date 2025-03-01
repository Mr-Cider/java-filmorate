package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.CreateValidation;
import ru.yandex.practicum.filmorate.service.UpdateValidation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate REALESE_FIRST_FILM = LocalDate.of(1895, 12, 28);
    //GITHUB не пропускает нормальные названия констант и модификатор static

    @PostMapping
    public Film createFilm(@Validated(CreateValidation.class) @RequestBody Film film, BindingResult bindingResult) {
        log.info("Добавляем фильм");
        Checkers.checkErrorValidation(bindingResult, log);
        log.trace("Валидация прошла успешно");
        checkRealeseFilm(film);
        log.debug("Присваиваем id");
        film.setId(getNextId());
        log.debug("Добавляем фильм в базу");
        films.put(film.getId(), film);
        log.info("Фильм добавлен в базу");
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PutMapping
    public Film updateFilm(@Validated(UpdateValidation.class) @RequestBody Film film, BindingResult bindingResult) {
        log.debug("Обновляем фильм");
        Checkers.checkErrorValidation(bindingResult, log);
        if (!(films.containsKey(film.getId()))) {
            log.error("ID фильма не найден");
            throw new ValidationException(HttpStatus.NOT_FOUND);
        }
        checkRealeseFilm(film);
        if (film.getName().isBlank()) {
            film.setName(films.get(film.getId()).getName());
        }
        if (film.getDescription().isBlank()) {
            film.setDescription(films.get(film.getId()).getDescription());
        }

        log.debug("Обновляем фильм в базе");
        films.put(film.getId(), film);
        log.info("Фильм обновлен");
        return film;
    }

    private void checkRealeseFilm(Film film) {
        if (film.getReleaseDate().isBefore(REALESE_FIRST_FILM)) {
            log.error("Фильм не может быть выпущен раньше {}",
                    REALESE_FIRST_FILM.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            throw new ValidationException(HttpStatus.BAD_REQUEST);
        }
    }

    private long getNextId() {
        log.trace("Присвоение id");
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
