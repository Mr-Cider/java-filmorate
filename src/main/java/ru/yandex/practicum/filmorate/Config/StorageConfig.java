package ru.yandex.practicum.filmorate.Config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserRowMapper;

import javax.sql.DataSource;

@Configuration
public class StorageConfig {

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:h2:file:./db/filmorate;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE");
        dataSource.setUsername("SA");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public FilmDbStorage filmDbStorage(JdbcTemplate jdbcTemplate,
                                       RowMapper<Film> filmRowMapper) {
        return new FilmDbStorage(jdbcTemplate, filmRowMapper);
    }

    @Bean
    public UserDbStorage userDbStorage(JdbcTemplate jdbc,
                                     RowMapper<User> mapper) {
        return new UserDbStorage(jdbc, mapper);
    }

    @Bean
    public RowMapper<Film> filmRowMapper() {
        return new FilmRowMapper();
    }

    @Bean
    public RowMapper<User> userRowMapper() {
        return new UserRowMapper();
    }
}