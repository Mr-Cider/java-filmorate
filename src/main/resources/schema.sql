DROP TABLE IF EXISTS film_genre;
DROP TABLE IF EXISTS film_likes;
DROP TABLE IF EXISTS friends;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS rate;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS users;

create table if not exists users (
                                     user_id bigint AUTO_INCREMENT primary key,
                                     email varchar(500),
                                     login varchar(500),
                                     name varchar(500),
                                     birthday timestamp
);

create table if not exists rate (
                                    rate_id INTEGER PRIMARY KEY,
                                    name VARCHAR(50) NOT NULL
);

create table if not exists films (
                                     film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                     name VARCHAR(100) NOT NULL,
                                     description VARCHAR(200),
                                     release_date DATE,
                                     duration INTEGER,
                                     rate_id INTEGER REFERENCES rate(rate_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS friends (
                                       user_id BIGINT NOT NULL,
                                       friend_id BIGINT NOT NULL,
                                       PRIMARY KEY (user_id, friend_id),
                                       FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                                       FOREIGN KEY (friend_id) REFERENCES users(user_id) ON DELETE CASCADE
);

create table if not exists film_likes (
                                          film_id bigint,
                                          user_id bigint,
                                          primary key (film_id, user_id)
);

create table if not exists genre (
                                     genre_id bigint AUTO_INCREMENT primary key,
                                     name varchar(500)
);

create table if not exists film_genre (
                                          film_id bigint,
                                          genre_id bigint,
                                          primary key (film_id, genre_id)
);

create index if not exists friends_idx on friends (user_id, friend_id);
create index if not exists film_likes_idx on film_likes (film_id);
create index if not exists film_genre_idx on film_genre (film_id, genre_id);

ALTER TABLE friends ADD CONSTRAINT friends_user_id_fk
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE friends ADD CONSTRAINT friends_friend_id_fk
    FOREIGN KEY (friend_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE film_likes ADD CONSTRAINT film_likes_user_id_fk
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE film_likes ADD CONSTRAINT film_likes_film_id_fk
    FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE;

ALTER TABLE film_genre ADD CONSTRAINT film_genre_film_id_fk
    FOREIGN KEY (film_id) REFERENCES films (film_id) ON DELETE CASCADE;

ALTER TABLE film_genre ADD CONSTRAINT film_genre_genre_id_fk
    FOREIGN KEY (genre_id) REFERENCES genre (genre_id) ON DELETE CASCADE;

ALTER TABLE films ADD CONSTRAINT film_rate_fk
    FOREIGN KEY (rate_id) REFERENCES rate (rate_id) ON DELETE SET NULL;