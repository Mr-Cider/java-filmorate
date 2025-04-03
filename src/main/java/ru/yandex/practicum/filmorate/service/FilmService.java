package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dto.GenreDto;
import ru.yandex.practicum.filmorate.storage.dto.MpaDto;
import ru.yandex.practicum.filmorate.storage.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.storage.dto.UpdateFilmRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final Long DEFAULT_MPA_ID = 1L;

    public Film createFilm(NewFilmRequest request) {
        validateMpa(request.getMpa());
        Film film = buildFilmFromRequest(request);
        film.setId(filmStorage.generateId());
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(UpdateFilmRequest request) {
        Film existingFilm = getFilmOrThrow(request.getId());
        Film updatedFilm = buildFilmFromRequest(request, existingFilm);
        return filmStorage.updateFilm(updatedFilm);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Optional<Film> getFilm(Long id) {
        return filmStorage.getFilm(id);
    }

    public List<Film> getTopFilms(Integer count) {
        return filmStorage.getTopFilms(count);
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

    private Film buildFilmFromRequest(NewFilmRequest request) {
        return Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .mpa(getMpaIdFromRequest(request))
                .genres(request.getGenres())
                .build();
    }

    private Film buildFilmFromRequest(UpdateFilmRequest request, Film existingFilm) {
        return Film.builder()
                .id(request.getId())
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .mpa(getMpaIdFromRequest(request, existingFilm))
                .rateMPA(Film.RateMPA.getById(getMpaIdFromRequest(request, existingFilm)))
                .genres(request.getGenres() != null ? request.getGenres() : existingFilm.getGenres())
                .build();
    }

    private Long getMpaIdFromRequest(NewFilmRequest request) {
        return request.getMpa() != null ? request.getMpa().getId() : DEFAULT_MPA_ID;
    }

    private Long getMpaIdFromRequest(UpdateFilmRequest request, Film existingFilm) {
        return request.getMpa() != null ? request.getMpa().getId() : existingFilm.getMpa();
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