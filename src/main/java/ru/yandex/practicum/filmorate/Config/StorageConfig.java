package ru.yandex.practicum.filmorate.Config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import javax.sql.DataSource;

@Configuration
public class StorageConfig {

    @Bean
    public FilmDbStorage filmDbStorage(JdbcTemplate jdbcTemplate,
                                       RowMapper<Film> filmRowMapper) {
        return new FilmDbStorage(jdbcTemplate, filmRowMapper);
    }

    @Bean
    public UserStorage userDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        return new UserDbStorage(jdbc, mapper);
    }


    @Bean
    @ConditionalOnProperty(name = "storage.type", havingValue = "memory")
    public FilmStorage inMemoryFilmStorage() {
        return new InMemoryFilmStorage();
    }

    @Bean
    @ConditionalOnProperty(name = "storage.type", havingValue = "memory")
    public UserStorage inMemoryUserStorage() {
        return new InMemoryUserStorage();
    }


    @Bean
    @ConditionalOnProperty(name = "storage.type", havingValue = "db")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    @ConditionalOnProperty(name = "storage.type", havingValue = "db")
    public RowMapper<Film> filmRowMapper() {
        return new FilmRowMapper();
    }

    @Bean
    @ConditionalOnProperty(name = "storage.type", havingValue = "db")
    public RowMapper<User> userRowMapper() {
        return new UserRowMapper();
    }
}

