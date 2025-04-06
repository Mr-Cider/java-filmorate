package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.dto.FilmDto;
import ru.yandex.practicum.filmorate.storage.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.storage.dto.UpdateFilmRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public FilmDto createFilm(@Valid @RequestBody NewFilmRequest request, BindingResult bindingResult) {
        Checkers.checkErrorValidation(bindingResult, log);
        return filmService.createFilm(request);
    }


    @PutMapping
    public FilmDto updateFilm(@Valid @RequestBody UpdateFilmRequest request, BindingResult bindingResult) {
        Checkers.checkErrorValidation(bindingResult, log);
        return filmService.updateFilm(request);
    }

    @GetMapping
    public List<FilmDto> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public FilmDto getFilmById(@PathVariable Long id) {
        return filmService.getFilm(id)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopFilms(count);
    }
}
