package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import java.util.List;

@Getter
public class AnnotationValidatorException extends RuntimeException {
    private final List<String> errors;

    public AnnotationValidatorException(List<String> errors) {
        this.errors = errors;
    }
}
