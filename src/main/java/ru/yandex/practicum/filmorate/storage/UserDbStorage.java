package ru.yandex.practicum.filmorate.storage;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseDbStorage<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_ALL_USER_ID_QUERY = "SELECT user_id FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
    private static final String GET_FRIENDS_QUERY = "SELECT u.* FROM users u JOIN friends f ON u.user_id = f.friend_id WHERE f.user_id = ?";
    private static final String GET_PENDING_FRIENDS_QUERY = "SELECT u.* FROM users u JOIN friends f ON u.user_id = f.friend_id WHERE f.user_id = ? AND f.accept_friend = false";
    private static final String FIND_COMMON_FRIENDS_QUERY = "SELECT u.* FROM users u JOIN friends f1 ON u.user_id = f1.friend_id JOIN friends f2 ON u.user_id = f2.friend_id WHERE f1.user_id = ? AND f2.user_id = ?";
    private static final String ACCEPT_FRIEND_QUERY = "UPDATE friends SET accept_friend = true WHERE user_id = ? AND friend_id = ?";
    private static final String REMOVE_FRIEND_QUERY = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String CHECK_FRIEND_EXISTS_QUERY = "SELECT COUNT(*) > 0 FROM friends WHERE user_id = ? AND friend_id = ?";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Long> getIds() {
        return getIds(FIND_ALL_USER_ID_QUERY);
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    @Override
    public User addUser(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Timestamp.from(user.getBirthday().atStartOfDay().toInstant(ZoneOffset.UTC)));
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Timestamp.from(user.getBirthday().atStartOfDay().toInstant(ZoneOffset.UTC)),
                user.getId());
        return user;
    }

    @Override
    @Transactional
    public void addFriend(Long userId, Long friendId) {
        Boolean exists = jdbc.queryForObject(CHECK_FRIEND_EXISTS_QUERY, Boolean.class, userId, friendId);
        if (Boolean.FALSE.equals(exists)) {
            jdbc.update(ADD_FRIEND_QUERY, userId, friendId);
        }
    }

    @Override
    public void acceptFriend(Long userId, Long friendId) {
        jdbc.update(ACCEPT_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        jdbc.update(REMOVE_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public List<User> getFriendsRequest(Long userId) {
        return findMany(GET_PENDING_FRIENDS_QUERY, userId);
    }

    @Override
    public List<User> getUsers() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        return findMany(FIND_COMMON_FRIENDS_QUERY, userId, friendId);
    }

    @Override
    public List<User> getFriends(long userId) {
        return findMany(GET_FRIENDS_QUERY, userId);
    }

    @Override
    public Long generateId() {
        return null;
    }
}