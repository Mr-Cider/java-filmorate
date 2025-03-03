package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHendler {

    @ExceptionHandler(AnnotationValidatorException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(AnnotationValidatorException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request",
                request.getDescription(false).replace("uri", ""));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleIdValidationException(ValidationException e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(e.getStatus().value(), e.getStatus().toString(),
                request.getDescription(false).replace("uri", ""));
        return new ResponseEntity<>(errorResponse, e.getStatus());
    }
}
