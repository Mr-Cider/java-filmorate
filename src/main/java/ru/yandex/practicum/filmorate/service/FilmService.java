package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RateMPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final Long DEFAULT_MPA_ID = 1L;

    public FilmDto createFilm(NewFilmRequest request) {
        validateMpa(request.getMpa());
        Film film = buildFilmFromRequest(request);
        Film createdFilm = filmStorage.addFilm(film);
        return convertToDto(createdFilm);
    }

    public FilmDto updateFilm(UpdateFilmRequest request) {
        Film existingFilm = getFilmOrThrow(request.getId());
        Film updatedFilm = buildFilmFromRequest(request, existingFilm);
        Film savedFilm = filmStorage.updateFilm(updatedFilm);
        return convertToDto(savedFilm);
    }

    public List<FilmDto> getFilms() {
        return filmStorage.getFilms().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<FilmDto> getFilm(Long id) {
        return filmStorage.getFilm(id)
                .map(this::convertToDto);
    }

    public List<FilmDto> getTopFilms(Integer count) {
        return filmStorage.getTopFilms(count).stream()
                .map(this::convertToDto)
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

    private FilmDto convertToDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .genres(film.getGenres())
                .mpa(film.getMpaForResponse())
                .build();
    }

    private Film buildFilmFromRequest(NewFilmRequest request) {
        return Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .mpa(getMpaIdFromRequest(request))
                .genres(toGenreList(request.getGenres()))
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
                .rateMPA(RateMPA.getById(getMpaIdFromRequest(request, existingFilm)))
                .genres(request.getGenres() != null ?
                        toGenreList(request.getGenres()) :
                        existingFilm.getGenres())
                .build();
    }

    private List<Genre> toGenreList(List<GenreDto> dtos) {
        if (dtos == null) {
            return null;
        }
        List<Genre> result = new ArrayList<>();
        for (GenreDto dto : dtos) {
            result.add(new Genre(dto.getId(), dto.getName()));
        }
        return result;
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