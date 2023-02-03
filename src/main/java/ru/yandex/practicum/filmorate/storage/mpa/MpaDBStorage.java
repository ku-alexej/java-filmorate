package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@Qualifier("MpaDBStorage")
public class MpaDBStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> allMpa() {
        final String sqlQuery = "select MPA_ID, MPA_NAME " +
                                "from MPAS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Mpa getMpa(int mpaId) {
        return getMpaFromDB(mpaId);
    }

    private Mpa getMpaFromDB(int mpaId) {
        String sqlQuery = "select count(MPA_ID) " +
                          "from MPAS " +
                          "where MPA_ID = ?";
        int count = jdbcTemplate.queryForObject(sqlQuery, new Object[]{mpaId}, Integer.class);
        if(count !=1)
        {
            return null;
        }

        sqlQuery = "select MPA_ID, MPA_NAME " +
                          "from MPAS " +
                          "where MPA_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, mpaId);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("MPA_ID"))
                .name(resultSet.getString("MPA_NAME"))
                .build();
    }
}
