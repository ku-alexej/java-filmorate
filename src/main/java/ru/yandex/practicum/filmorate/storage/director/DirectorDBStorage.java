package ru.yandex.practicum.filmorate.storage.director;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Qualifier("directorDBStorage")
public class DirectorDBStorage implements DirectorStorage {

    private final JdbcTemplate jdbcTemplate;

    public DirectorDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> getAll() {
        String sqlQuery = "SELECT d.id, " +
                "d.name " +
                "FROM directors AS d;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeDirector(rs));
    }

    @Override
    public Director getById(Long id) throws EntityNotFoundException {
        String sqlQuery = "SELECT d.id, " +
                "d.name " +
                "FROM directors AS d " +
                "WHERE d.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeDirector(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Director with ID " + id + " does not exist");
        }
    }

    @Override
    public Director add(Director director) {
        String sqlQuery = "INSERT INTO directors (name) VALUES (?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setString(1, director.getName());
            return statement;
        }, keyHolder);

        director.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return director;
    }

    @Override
    public Director update(Director director) {
        String sqlQuery = "UPDATE directors SET name = ? WHERE id = ?;";
        jdbcTemplate.update(
                sqlQuery,
                director.getName(),
                director.getId()
        );
        return getById(director.getId());
    }

    @Override
    public void delete(Long id) {
        String sqlQuery = "DELETE FROM directors WHERE id = ?;";
        jdbcTemplate.update(sqlQuery, id);
    }

    private Director makeDirector(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Director(id, name);
    }
}
