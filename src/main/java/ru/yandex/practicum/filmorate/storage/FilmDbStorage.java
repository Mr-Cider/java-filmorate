package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dto.GenreDto;
import ru.yandex.practicum.filmorate.storage.dto.MpaDto;

import java.util.*;
import java.util.stream.Collectors;

public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String find_all_query = "SELECT * FROM films";
    private static final String find_all_film_id_query = "SELECT film_id FROM films";
    private static final String find_by_id_query = "SELECT * FROM films WHERE film_id = ?";
    private static final String find_top_films = "SELECT f.* FROM films f LEFT JOIN film_likes fl ON f.film_id = fl.film_id GROUP BY f.film_id ORDER BY COUNT(fl.user_id) DESC LIMIT ?";
    private static final String insert_query = "INSERT INTO films (name, description, release_date, duration, rate_id) VALUES (?, ?, ?, ?, ?)";
    private static final String update_query = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rate_id = ? WHERE film_id = ?";
    private static final String get_all_genres = "SELECT * FROM genre";
    private static final String get_genre_by_id = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String get_all_mpa = "SELECT * FROM rate";
    private static final String get_mpa_by_id = "SELECT * FROM rate WHERE rate_id = ?";
    private static final String add_like = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
    private static final String remove_like = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
    private static final String get_film_genres = "SELECT genre_id FROM film_genre WHERE film_id = ?";
    private static final String add_genre = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
    private static final String clear_genres = "DELETE FROM film_genre WHERE film_id = ?";
    private static final String load_film_genres = "SELECT g.genre_id as id, g.name as name FROM film_genre fg JOIN genre g ON fg.genre_id = g.genre_id WHERE fg.film_id = ?";
    private static final String load_film_rate = "SELECT rate_id FROM films WHERE film_id = ?";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Long> getIds() {
        return getIds(find_all_film_id_query);
    }

    @Override
    public Optional<Film> getFilm(Long id) {
        Optional<Film> film = findOne(find_by_id_query, id);
        film.ifPresent(this::loadFilmData);
        return film;
    }

    @Override
    public Film addFilm(Film film) {
        long id = insert(
                insert_query,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() != null ? film.getMpa() : 1L
        );
        film.setId(id);

        if (film.getGenres() != null) {
            Set<Long> uniqueGenreIds = film.getGenres().stream()
                    .map(GenreDto::getId)
                    .collect(Collectors.toSet());

            uniqueGenreIds.forEach(genreId ->
                    jdbc.update(add_genre, id, genreId));

            film.setGenres(uniqueGenreIds.stream()
                    .map(genreId -> new GenreDto(genreId, null))
                    .collect(Collectors.toList()));
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(
                update_query,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa(),
                film.getId()
        );

        jdbc.update(clear_genres, film.getId());

        if (film.getGenres() != null) {
            film.getGenres().stream()
                    .map(GenreDto::getId)
                    .distinct()
                    .forEach(genreId -> jdbc.update(add_genre, film.getId(), genreId));
        }

        return film;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = findMany(find_all_query);
        films.forEach(this::loadFilmData);
        return films;
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return findMany(find_top_films, count);
    }

    @Override
    public List<GenreDto> getAllGenres() {
        return jdbc.query(get_all_genres, (rs, rowNum) ->
                new GenreDto(rs.getLong("genre_id"), rs.getString("name")));
    }

    public Optional<GenreDto> getGenreById(Long id) {
        try {
            GenreDto genre = jdbc.queryForObject(get_genre_by_id, (rs, rowNum) ->
                    new GenreDto(rs.getLong("genre_id"), rs.getString("name")), id);
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<MpaDto> getAllMpa() {
        return jdbc.query(get_all_mpa, (rs, rowNum) ->
                new MpaDto(rs.getLong("rate_id"), rs.getString("name")));
    }

    public Optional<MpaDto> getMpaById(Long id) {
        try {
            MpaDto mpa = jdbc.queryForObject(get_mpa_by_id, (rs, rowNum) ->
                    new MpaDto(rs.getLong("rate_id"), rs.getString("name")), id);
            return Optional.ofNullable(mpa);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        jdbc.update(add_like, filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        jdbc.update(remove_like, filmId, userId);
    }

    @Override
    public Long generateId() {
        return null;
    }

    private void loadFilmData(Film film) {
        List<GenreDto> genres = jdbc.query(load_film_genres,
                (rs, rowNum) -> new GenreDto(rs.getLong("id"), rs.getString("name")),
                film.getId()
        );
        film.setGenres(genres != null ? genres : new ArrayList<>());

        Long rateId = jdbc.queryForObject(load_film_rate, Long.class, film.getId());

        if (rateId != null) {
            film.setMpa(rateId);
            film.setRateMPA(Film.RateMPA.getById(rateId));
        } else {
            film.setMpa(1L);
            film.setRateMPA(Film.RateMPA.getById(1L));
        }
    }
}