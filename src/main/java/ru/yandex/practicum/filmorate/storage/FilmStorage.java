package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dto.GenreDto;
import ru.yandex.practicum.filmorate.storage.dto.MpaDto;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> getFilms();

    Optional<Film> getFilm(Long id);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getTopFilms(Integer count);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<GenreDto> getAllGenres();

    Optional<GenreDto> getGenreById(Long id);

    List<MpaDto> getAllMpa();

    Optional<MpaDto> getMpaById(Long id);

    List<Long> getIds();
}
