package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Arrays;

@Getter
public enum RateMPA {
    G(1, "G"),
    PG(2, "PG"),
    PG_13(3, "PG-13"),
    R(4, "R"),
    NC_17(5, "NC-17");

    private final long id;
    private final String name;

    RateMPA(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getDisplayName() {
        return name;
    }

    public static RateMPA getById(Long id) {
        return Arrays.stream(values())
                .filter(rate -> rate.id == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("rate_id не найден"));
    }
}