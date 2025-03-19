package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film getFilm(Long id) {
        return films.get(id);
    }

    @Override
    public void addOrUpdateFilm(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        return getFilms().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getListUsersLikeId().size())
                        .reversed()).limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getIds() {
        return new ArrayList<>(films.keySet());
    }
}
