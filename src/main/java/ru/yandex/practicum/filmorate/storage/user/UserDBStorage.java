package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@Qualifier("UserDBStorage")
public class UserDBStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> allUsers() {
        final String sqlQuery = "select USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY from USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User addUser(User user) {
        final String sqlQuery = "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            final LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        log.debug("Новому пользователю присвоен ID: {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        changeUser(user);
        log.debug("Обновлен пользователь с ID: {}", user.getId());
        return user;
    }

    @Override
    public User changeUser(User user) {
        final String sqlQuery = "update USERS set " +
                "EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ?" +
                "where USER_ID = ?";

        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public long deleteUser(long userId) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, userId);
        log.debug("Удален пользователь с ID: {}", userId);
        sqlQuery = "delete from LIKES where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, userId);
        log.debug("Удалены лайки пользователя с ID: {}", userId);
        sqlQuery = "delete from FRIENDS where USER_ID = ? or FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, userId);
        log.debug("Удалена дружба с пользователем с ID: {}", userId);
        return userId;
    }

    @Override
    public void addFriend(User user, User friend) {
        final String sqlQuery = "insert into FRIENDS (USER_ID, FRIEND_ID) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"REQUEST_ID"});
            stmt.setLong(1, user.getId());
            stmt.setLong(2, friend.getId());
            return stmt;
        }, keyHolder);
        log.debug("Дружба добавлена");
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        String sqlQuery = "delete from FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        log.debug("Дружба удалена");
    }

    @Override
    public List<User> getFriends(long userId) {
        String sqlQuery = "select FRIEND_ID as USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY " +
                        "from USERS as U join FRIENDS F on U.USER_ID = F.FRIEND_ID " +
                        "where F.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public User getUser(long userId) {
        User userFromDB = getUserFromDB(userId);
        return userFromDB;
    }

    private User getUserFromDB(long userId) {
        String sqlQuery = "SELECT count(USER_ID) FROM USERS WHERE USER_ID = ?";
        int count = jdbcTemplate.queryForObject(sqlQuery, new Object[] {userId}, Integer.class);
        if (count != 1) {
            return null;
        }
        sqlQuery = "select USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY from USERS where USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .name(resultSet.getString("USER_NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}