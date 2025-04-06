package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dto.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final DataTransformer dataTransformer;


    public FilmDto createFilm(NewFilmRequest request) {
        validateMpa(request.getMpa());
        Film film = dataTransformer.buildFilmFromRequest(request);
        Film createdFilm = filmStorage.addFilm(film);
        return dataTransformer.convertToFilmDto(createdFilm);
    }

    public FilmDto updateFilm(UpdateFilmRequest request) {
        Film existingFilm = getFilmOrThrow(request.getId());
        Film updatedFilm = dataTransformer.buildFilmFromRequest(request, existingFilm);
        Film savedFilm = filmStorage.updateFilm(updatedFilm);
        return dataTransformer.convertToFilmDto(savedFilm);
    }

    public List<FilmDto> getFilms() {
        return filmStorage.getFilms().stream()
                .map(dataTransformer::convertToFilmDto)
                .collect(Collectors.toList());
    }

    public Optional<FilmDto> getFilm(Long id) {
        return filmStorage.getFilm(id)
                .map(dataTransformer::convertToFilmDto);
    }

    public List<FilmDto> getTopFilms(Integer count) {
        return filmStorage.getTopFilms(count).stream()
                .map(dataTransformer::convertToFilmDto)
                .collect(Collectors.toList());
    }

    public void addLike(Long filmId, Long userId) {
        validateFilmAndUserExist(filmId, userId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        validateFilmAndUserExist(filmId, userId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<GenreDto> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public GenreDto getGenreById(Long id) {
        return filmStorage.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id " + id + " не найден"));
    }

    public List<MpaDto> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public MpaDto getMpaById(Long id) {
        return filmStorage.getMpaById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id " + id + " не найден"));
    }

    private void validateMpa(MpaDto mpa) {
        if (mpa != null) {
            getMpaById(mpa.getId());
        }
    }

    private Film getFilmOrThrow(Long id) {
        return filmStorage.getFilm(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
    }

    private void validateUserExists(Long id) {
        if (userStorage.getUser(id).isEmpty()) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
    }

    private void validateFilmAndUserExist(Long filmId, Long userId) {
        getFilmOrThrow(filmId);
        validateUserExists(userId);
    }
}