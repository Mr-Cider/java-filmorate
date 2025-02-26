package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;
import java.util.List;

@Getter
public class AnnotationValidationException extends RuntimeException {
    private final List<String> errors;

    public AnnotationValidationException(List<String> errors){
        this.errors = errors;
    }
}
