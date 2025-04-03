package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;
import java.util.stream.Collectors;
import ru.yandex.practicum.filmorate.storage.dto.GenreDto;
import ru.yandex.practicum.filmorate.storage.dto.MpaDto;

public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Set<Long>> filmGenres = new HashMap<>();
    private long currentId = 1;

    private final Map<Long, GenreDto> genres = Map.of(
            1L, new GenreDto(1L, "Комедия"),
            2L, new GenreDto(2L, "Драма"),
            3L, new GenreDto(3L, "Мультфильм"),
            4L, new GenreDto(4L, "Триллер"),
            5L, new GenreDto(5L, "Документальный"),
            6L, new GenreDto(6L, "Боевик")
    );

    private final Map<Long, MpaDto> mpaRatings = Map.of(
            1L, new MpaDto(1L, "G"),
            2L, new MpaDto(2L, "PG"),
            3L, new MpaDto(3L, "PG-13"),
            4L, new MpaDto(4L, "R"),
            5L, new MpaDto(5L, "NC-17")
    );

    @Override
    public List<Long> getIds() {
        return new ArrayList<>(films.keySet());
    }

    @Override
    public Optional<Film> getFilm(Long id) {
        return Optional.of(films.get(id));
    }

    @Override
    public Film addFilm(Film film) {
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return films.values().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getListUsersLikeId().size(), f1.getListUsersLikeId().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Long generateId() {
        return currentId++;
    }

    @Override
    public List<GenreDto> getAllGenres() {
        return new ArrayList<>(genres.values());
    }

    @Override
    public Optional<GenreDto> getGenreById(Long id) {
        return Optional.ofNullable(genres.get(id));
    }

    @Override
    public List<MpaDto> getAllMpa() {
        return new ArrayList<>(mpaRatings.values());
    }

    @Override
    public Optional<MpaDto> getMpaById(Long id) {
        return Optional.ofNullable(mpaRatings.get(id));
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        Film film = films.get(filmId);
        if (film != null) {
            film.getListUsersLikeId().add(userId);
        }
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        Film film = films.get(filmId);
        if (film != null) {
            film.getListUsersLikeId().remove(userId);
        }
    }
}
