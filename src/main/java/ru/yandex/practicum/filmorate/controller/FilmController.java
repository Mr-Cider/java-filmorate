package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final LocalDate realeseFirstFilm = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        log.info("Добавляем фильм");
        Checkers.checkErrorValidation(bindingResult, log);
        log.trace("Валидация прошла успешно");
        if (film.getReleaseDate().isBefore(realeseFirstFilm)) {
            log.error("Фильм не может быть выпущен раньше {} ",
                    realeseFirstFilm.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            throw new ValidationException(HttpStatus.BAD_REQUEST);
        }
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
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        log.debug("Обновляем фильм");
        Checkers.checkErrorValidation(bindingResult, log);
        if (!(films.containsKey(film.getId()))) {
            log.error("ID фильма не найден");
            throw new ValidationException(HttpStatus.NOT_FOUND);
        }

        if (film.getReleaseDate().isBefore(realeseFirstFilm)) {
            log.error("Фильм не может быть выпущен раньше {}",
                    realeseFirstFilm.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            throw new ValidationException(HttpStatus.BAD_REQUEST);
        }
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
