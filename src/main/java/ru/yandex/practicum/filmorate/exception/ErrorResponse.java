package ru.yandex.practicum.filmorate.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime timestamp;
    private final int status;
    private final String error;
    private final String path;

    public ErrorResponse(int status, String error, String path) {
        this.timestamp = ZonedDateTime.now();
        this.status = status;
        this.error = error;
        this.path = path;
    }
}
