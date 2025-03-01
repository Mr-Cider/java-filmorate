package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private FilmController filmController;
    Film film;

    private TestRestTemplate restTemplate;
    private String url;

    @BeforeEach
    void beforeEach() {
        film = new Film();
        restTemplate = new TestRestTemplate();
        url = "http://localhost:" + port + "/films";
    }

    @Test
    void shouldCreateOkFilm() {
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2025, 1, 1));
        film.setDuration(100);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Name", response.getBody().getName());
    }

    @Test
    void shouldGetNull() {
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldCreateFilmInvalidReleaseDate() {
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1799, 1, 1));
        film.setDuration(100);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldCreateFilmRealeseDateOnRealeseFirstFilm() {
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldCreateFilmOnNegativeDuration() {
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        film.setDuration(-100);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldCreateFilmOnDurationIsNull() {
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldCreateFilmOnDurationIs0() {
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        film.setDuration(0);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldCreateFilmOnNameIsNull() {
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        film.setDuration(100);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldCreateFilmOnNameIsSpace() {
        film.setName(" ");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        film.setDuration(100);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldCreateFilmOnDescriptionIsNull() {
        film.setName("Name");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        film.setDuration(100);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldCreateFilmOnDescriptionIs200Chars() {
        film.setName("Name");
        film.setDescription("D".repeat(200));
        film.setReleaseDate(LocalDate.of(2025, 1, 1));
        film.setDuration(100);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldCreateFilmOnDescriptionIs201Chars() {
        film.setName("Name");
        film.setDescription("D".repeat(201));
        film.setReleaseDate(LocalDate.of(2025, 1, 1));
        film.setDuration(100);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldUpdateFilmIsOk() {
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        film.setDuration(100);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Film film2 = new Film();
        film2.setId(response.getBody().getId());
        film2.setName("New Name");
        film2.setDescription("New Description");
        film2.setReleaseDate(LocalDate.of(2025, 1, 1));
        film2.setDuration(200);
        HttpEntity<Film> entity = new HttpEntity<>(film2);
        ResponseEntity<Film> updateResponse = restTemplate.exchange(url, HttpMethod.PUT, entity, Film.class);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals(film2.getName(), updateResponse.getBody().getName());
        assertEquals(film2.getId(), updateResponse.getBody().getId());
        assertEquals(film2.getDuration(), updateResponse.getBody().getDuration());
        assertEquals(film2.getReleaseDate(), updateResponse.getBody().getReleaseDate());
    }

    @Test
    void shouldUpdateFilmIsInvalidName() {
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        film.setDuration(100);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Film film2 = new Film();
        film2.setId(response.getBody().getId());
        film2.setName(" ");
        film2.setDescription("New Description");
        film2.setReleaseDate(LocalDate.of(2025, 1, 1));
        film2.setDuration(200);
        System.out.println(film2.toString());
        HttpEntity<Film> entity = new HttpEntity<>(film2);
        ResponseEntity<Film> updateResponse = restTemplate.exchange(url, HttpMethod.PUT, entity, Film.class);
        assertEquals(HttpStatus.BAD_REQUEST, updateResponse.getStatusCode());
    }

    @Test
    void shouldUpdateFilmIsInvalidId() {
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 12, 28));
        film.setDuration(100);
        ResponseEntity<Film> response = restTemplate.postForEntity(url, film, Film.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Film film2 = new Film();
        film2.setId(998);
        film2.setName("UpdateName");
        film2.setDescription("UpdateDescription");
        film2.setReleaseDate(LocalDate.of(2025, 1, 1));
        film2.setDuration(200);
        HttpEntity<Film> entity = new HttpEntity<>(film2);
        ResponseEntity<Film> updateResponse = restTemplate.exchange(url, HttpMethod.PUT, entity, Film.class);
        assertEquals(HttpStatus.NOT_FOUND, updateResponse.getStatusCode());
    }
}
