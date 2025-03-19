package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    public List<Long> getIds();

    public Film getFilm(Long id);

    public void addOrUpdateFilm(Film film);

    public List<Film> getFilms();

    public List<Film> getTopFilms(Integer count);
}
