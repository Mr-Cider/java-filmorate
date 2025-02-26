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
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private UserController userController;
    User user;

    private TestRestTemplate restTemplate;
    private String url;

    @BeforeEach
    void beforeEach() {
        user = new User();
        restTemplate = new TestRestTemplate();
        url = "http://localhost:" + port + "/users";
    }

    @Test
    void shouldCreateUserOk() {
        user.setLogin("Login");
        user.setName("Name");
        user.setEmail("ololo@gmail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void shouldCreateUserInvalidEmail() {
        user.setLogin("Login");
        user.setName("Name");
        user.setEmail("olologmail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldCreateUserINvalidLogin() {
        user.setLogin("");
        user.setName("Name");
        user.setEmail("ololo@gmail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldCreateUserNameIsEmpty() {
        user.setLogin("Login");
        user.setEmail("ololo@gmail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getLogin(), response.getBody().getName());
    }

    @Test
    void shouldCreateUserNameIsSpace() {
        user.setLogin("Login");
        user.setName(" ");
        user.setEmail("ololo@gmail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getLogin(), response.getBody().getName());
    }

    @Test
    void shouldCreateUserWithSpaceInLogin() {
        user.setLogin("Login Loginovich");
        user.setEmail("ololo@gmail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldCreateUserWithInvalidBirthday() {
        user.setLogin("Login");
        user.setName("Name");
        user.setEmail("ololo@gmail.com");
        user.setBirthday(LocalDate.of(2048, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldUpdateUserOk() {
        user.setLogin("Login");
        user.setName("Name");
        user.setEmail("ololo@gmail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user2 = new User();
        user2.setId(response.getBody().getId());
        user2.setLogin("UpdateLogin");
        user2.setName("UpdateName");
        user2.setEmail("updateOlolo@gmail.com");
        user2.setBirthday(LocalDate.of(2001, 2, 2));
        HttpEntity<User> entity = new HttpEntity<>(user2);
        ResponseEntity<User> updateResponse = restTemplate.exchange(url, HttpMethod.PUT, entity, User.class);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals(response.getBody().getId(), updateResponse.getBody().getId());
        assertEquals(user2.getName(), updateResponse.getBody().getName());
        assertEquals(user2.getLogin(), updateResponse.getBody().getLogin());
        assertEquals(user2.getEmail(), updateResponse.getBody().getEmail());
        assertEquals(user2.getBirthday(), updateResponse.getBody().getBirthday());
    }

    @Test
    void shouldUpdateUserWithInvalidLogin() {
        user.setLogin("Login");
        user.setName("Name");
        user.setEmail("ololo@gmail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user2 = new User();
        user2.setId(response.getBody().getId());
        user2.setLogin("Update Login");
        user2.setName("UpdateName");
        user2.setEmail("updateOlolo@gmail.com");
        user2.setBirthday(LocalDate.of(2001, 2, 2));
        HttpEntity<User> entity = new HttpEntity<>(user2);
        ResponseEntity<User> updateResponse = restTemplate.exchange(url, HttpMethod.PUT, entity, User.class);
        assertEquals(HttpStatus.BAD_REQUEST, updateResponse.getStatusCode());
    }

    @Test
    void shouldUpdateUserWithInvalidId() {
        user.setLogin("Login");
        user.setName("Name");
        user.setEmail("ololo@gmail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user2 = new User();
        user2.setId(998);
        user2.setLogin("UpdateLogin");
        user2.setName("UpdateName");
        user2.setEmail("updateOlolo@gmail.com");
        user2.setBirthday(LocalDate.of(2001, 2, 2));
        HttpEntity<User> entity = new HttpEntity<>(user2);
        ResponseEntity<User> updateResponse = restTemplate.exchange(url, HttpMethod.PUT, entity, User.class);
        assertEquals(HttpStatus.NOT_FOUND, updateResponse.getStatusCode());
    }
}
