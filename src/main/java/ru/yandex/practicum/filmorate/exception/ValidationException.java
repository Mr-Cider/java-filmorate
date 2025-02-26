package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidationException extends RuntimeException {
    private final HttpStatus status;

    public ValidationException(HttpStatus status) {
        this.status = status;
    }
}
