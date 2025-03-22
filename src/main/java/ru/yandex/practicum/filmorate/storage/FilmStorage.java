package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Long> getIds();

    Film getFilm(Long id);

    void addOrUpdateFilm(Film film);

    List<Film> getFilms();

    List<Film> getTopFilms(Integer count);
}
