package ru.yandex.practicum.filmorate.storage.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@Qualifier("GenreDBStorage")
public class GenreDBStorage implements GenreStorage{

    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> allGenres() {
        final String sqlQuery = "select GENRE_ID, GENRE_NAME " +
                                "from GENRES";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getGenre(int genreId) {
        return getGenreFromDB(genreId);
    }

    private Genre getGenreFromDB(int genreId) {
        String sqlQuery = "select count(GENRE_ID) " +
                        "from GENRES " +
                        "where GENRE_ID = ?";
        int count = jdbcTemplate.queryForObject(sqlQuery, new Object[]{genreId}, Integer.class);
        if(count !=1)
        {
            return null;
        }

        sqlQuery = "select GENRE_ID, GENRE_NAME " +
                "from GENRES " +
                "where GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }
}
