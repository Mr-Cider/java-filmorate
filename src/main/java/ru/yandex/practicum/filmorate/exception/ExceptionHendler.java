package ru.yandex.practicum.filmorate.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionHendler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(AnnotationValidatorException e, WebRequest request) {
        return new ErrorResponse(e.getMessage(),
                request.getDescription(false).replace("uri", ""));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIdValidationException(ValidationException e, WebRequest request) {
        return new ErrorResponse(e.getMessage(),
                request.getDescription(false).replace("uri", ""));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e, WebRequest request) {
        return new ErrorResponse(e.getMessage(),
                request.getDescription(false).replace("uri", ""));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException e, WebRequest request) {
        return new ErrorResponse("Связанная сущность не найдена",
                request.getDescription(false).replace("uri", ""));
    }
}

