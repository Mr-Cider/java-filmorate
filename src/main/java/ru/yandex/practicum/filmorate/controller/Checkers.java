package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exception.AnnotationValidatorException;
import java.util.List;
import java.util.stream.Collectors;

public class Checkers {

    public static void checkErrorValidation(BindingResult bindingResult, Logger log) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            errors.forEach(log::error);
            throw new AnnotationValidatorException(errors);
        }
    }
}

