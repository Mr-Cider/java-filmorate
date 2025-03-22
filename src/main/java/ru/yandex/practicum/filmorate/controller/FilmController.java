package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.customAnnotation.CreateValidation;
import ru.yandex.practicum.filmorate.customAnnotation.UpdateValidation;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    //добавить логирование
    private static final LocalDate REALESE_FIRST_FILM = LocalDate.of(1895, 12, 28);

    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@Validated(CreateValidation.class) @RequestBody Film film, BindingResult bindingResult) {
        log.info("Создаем пользователя");
        Checkers.checkErrorValidation(bindingResult, log);
        checkRealeseFilm(film);
        log.debug("Валидация прошла успешно");
        return filmService.createFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @PutMapping
    public Film updateFilm(@Validated(UpdateValidation.class) @RequestBody Film film, BindingResult bindingResult) {
        log.info("Обновляем фильм");
        Checkers.checkErrorValidation(bindingResult, log);
        log.debug("Валидация прошла успешно");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public long likeFilm(@PathVariable long userId, @PathVariable long filmId) {
        log.info("Ставим лайк");
        return filmService.giveLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public long deleteLikeFilm(@PathVariable long userId, @PathVariable long filmId) {
        log.info("Удаляем лайк");
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Выводим топ-{} фильмов", count);
        return filmService.getTopFilms(count);
    }

    private void checkRealeseFilm(Film film) {
        if (film.getReleaseDate().isBefore(REALESE_FIRST_FILM)) {
            log.error("Фильм не может быть выпущен раньше {}",
                    REALESE_FIRST_FILM.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            throw new ValidationException("Некорректная дата релиза фильма");
        }
    }
}
