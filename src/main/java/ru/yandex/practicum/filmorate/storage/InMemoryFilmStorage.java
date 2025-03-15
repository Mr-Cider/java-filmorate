package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate REALESE_FIRST_FILM = LocalDate.of(1895, 12, 28);

    public Film getFilm(Long id) {
        return films.get(id);
    }

    public Film createFilm(Film film) {
        log.info("Добавляем фильм");
        checkRealeseFilm(film);
        log.debug("Присваиваем id");
        film.setId(getNextId());
        log.debug("Добавляем фильм в базу");
        films.put(film.getId(), film);
        log.info("Фильм добавлен в базу");
        return film;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Film updateFilm(Film film) {
        log.debug("Обновляем фильм");
        if (!(films.containsKey(film.getId()))) {
            log.error("ID фильма не найден");
            throw new NotFoundException("ID фильма не найден");
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
            throw new ValidationException("Некорректная дата релиза фильма");
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
