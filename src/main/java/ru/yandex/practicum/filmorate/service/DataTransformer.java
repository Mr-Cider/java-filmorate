package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RateMPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dto.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataTransformer {

    private static final Long DEFAULT_MPA_ID = 1L;


    public FilmDto convertToFilmDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .genres(convertGenresToDto(film.getGenres()))
                .mpa(convertMpaToDto(film.getMpaRating()))
                .build();
    }

    public Film buildFilmFromRequest(NewFilmRequest request) {
        return Film.builder()
                .name(request.getName())
                .description(request.getDescription())
                .releaseDate(request.getReleaseDate())
                .duration(request.getDuration())
                .mpa(getMpaIdFromRequest(request))
                .genres(toGenreList(request.getGenres()))
                .build();
    }

    public Film buildFilmFromRequest(UpdateFilmRequest request, Film existingFilm) {
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

    public List<Genre> toGenreList(List<GenreDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(dto -> new Genre(dto.getId(), dto.getName()))
                .collect(Collectors.toList());
    }

    public Long getMpaIdFromRequest(NewFilmRequest request) {
        return request.getMpa() != null ? request.getMpa().getId() : DEFAULT_MPA_ID;
    }

    public Long getMpaIdFromRequest(UpdateFilmRequest request, Film existingFilm) {
        return request.getMpa() != null ? request.getMpa().getId() : existingFilm.getMpa();
    }

    public User convertToUser(NewUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .login(request.getLogin())
                .name(request.getName())
                .birthday(request.getBirthday())
                .build();
    }

    public UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }

    private MpaDto convertMpaToDto(RateMPA rateMPA) {
        return new MpaDto(rateMPA.getId(), rateMPA.getDisplayName());
    }

    private List<GenreDto> convertGenresToDto(List<Genre> genres) {
        return genres != null ?
                genres.stream()
                        .map(g -> new GenreDto(g.getId(), g.getName()))
                        .collect(Collectors.toList()) :
                null;
    }
}
