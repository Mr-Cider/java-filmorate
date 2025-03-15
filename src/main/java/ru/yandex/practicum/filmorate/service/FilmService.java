package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

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

    public List<Film> getTop10Films(Integer count) {
        return filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getListUsersLikeId().size())
                        .reversed()).limit(count)
                .collect(Collectors.toList());
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

