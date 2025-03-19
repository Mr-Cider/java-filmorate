package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    public Film createFilm(Film film) {
        log.info("Добавляем фильм");
        film.setId(getNextId());
        log.debug("Добавляем фильм в базу");
        filmStorage.addOrUpdateFilm(film);
        log.info("Фильм добавлен в базу");
        return film;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film updateFilm(Film film) {
        log.debug("Обновляем фильм");
        if (filmStorage.getFilm(film.getId()) == null) {
            log.error("ID фильма не найден");
            throw new NotFoundException("ID фильма не найден");
        }
        if (film.getName().isBlank()) {
            film.setName(filmStorage.getFilm(film.getId()).getName());
        }
        if (film.getDescription().isBlank()) {
            film.setDescription(filmStorage.getFilm(film.getId()).getDescription());
        }
        filmStorage.addOrUpdateFilm(film);
        return film;
    }

    public long giveLike(Long filmId, Long userId) {
        Film film = checkFilm(filmId);
        checkUser(userId);
        film.addUserLike(userId);
        return film.getListUsersLikeId().size();
    }

    public long removeLike(Long filmId, Long userId) {
        Film film = checkFilm(filmId);
        checkUser(userId);
        film.removeUserLike(userId);
        return film.getListUsersLikeId().size();
    }

    public List<Film> getTopFilms(Integer count) {
        return filmStorage.getTopFilms(count);
    }

    private long getNextId() {
        log.trace("Присвоение id");
        long currentMaxId = filmStorage.getIds()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private Film checkFilm(long filmId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден.");
        }
        return film;
    }

    private void checkUser(long userId) {
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден.");
        }
    }
}

