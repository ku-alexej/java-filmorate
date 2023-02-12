package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
@Qualifier("FilmDBStorage")
public class FilmDBStorage implements FilmStorage{

    private final JdbcTemplate jdbcTemplate;

    public FilmDBStorage(JdbcTemplate jdbcTemplate) {
         this.jdbcTemplate = jdbcTemplate;
     }

    @Autowired
    @Qualifier("MpaDBStorage")
    private MpaStorage mpaStorage;

    @Override
    public List<Film> allFilms() {
        final String sqlQuery = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID " +
                                "from FILMS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film addFilm(Film film) {
        final String sqlQuery = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID) " +
                                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4,film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        if (film.getGenres() != null) {
            updateFilmGenres(film);
        }
        log.debug("Новому фильму присвоен ID: {}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        changeFilm(film);
        if (film.getGenres() == null) {
            deleteFilmGenres(film);
        } else {
            updateFilmGenres(film);
        }
        log.debug("Обновлен фильм с ID: {}", film.getId());
        return getFilm(film.getId());
    }

    private void updateFilmGenres(Film film) {
        deleteFilmGenres(film);
        addFilmGenres(film);
    }

    private void addFilmGenres(Film film) {
        final String sqlQuery = "insert into FILMS_GENRES (FILM_ID, GENRE_ID) " +
                                "values (?, ?) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Set<Genre> genres = film.getGenres();

        for (Genre genre : genres) {
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILMS_GENRE_ID"});
                stmt.setLong(1, film.getId());
                stmt.setInt(2, genre.getId());
                return stmt;
            }, keyHolder);
        }
    }

    private void deleteFilmGenres(Film film) {
        String sqlQuery = "delete from FILMS_GENRES " +
                          "where FILM_ID = ?";
        int i = jdbcTemplate.update(sqlQuery, film.getId());
        log.debug("Для фильма с ID {} удалено {} жанров", film.getId(), i);
    }

    @Override
    public Film changeFilm(Film film) {
        final String sqlQuery = "update FILMS set " +
                                "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ?" +
                                "where FILM_ID = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public void deleteFilm(long filmId) {
        String sqlQuery = "delete from FILMS " +
                          "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        log.debug("Удален фильм с ID: {}", filmId);
        sqlQuery = "delete from LIKES " +
                   "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        log.debug("Удалены лайки для фильма с ID: {}", filmId);
        sqlQuery = "delete from FILMS_GENRES " +
                   "where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, filmId);
        log.debug("Удалена жанры фильма с ID: {}", filmId);
    }

    @Override
    public void addLike(long filmId, long userId) {
        final String sqlQuery = "insert into LIKES (FILM_ID, USER_ID) " +
                                "values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"LIKE_ID"});
            stmt.setLong(1, filmId);
            stmt.setLong(2, userId);
            return stmt;
        }, keyHolder);
        log.debug("Лайк поставлен");
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String sqlQuery = "delete from LIKES " +
                          "where FILM_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.debug("Лайк удален");
    }

    @Override
    public List<Film> getPopular(long count) {
        return null;
    }

    @Override
    public Film getFilm(long filmId) {
        return getFilmFromDB(filmId);
    }

    private Film getFilmFromDB(long filmId) {
        String sqlQuery = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID " +
                   "from FILMS " +
                   "where FILM_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
               .id(resultSet.getLong("FILM_ID"))
               .name(resultSet.getString("FILM_NAME"))
               .description(resultSet.getString("DESCRIPTION"))
               .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
               .duration(resultSet.getInt("DURATION"))
               .mpa(mpaStorage.getMpa(resultSet.getInt("MPA_ID")))
               .genres(getFilmGenresFromDB(resultSet.getLong("FILM_ID")))
               .usersId(getUsersLikesFromDB(resultSet.getLong("FILM_ID")))
               .build();
    }

    private Set<Genre> getFilmGenresFromDB(long filmId) {
        String sqlQuery = "select G.GENRE_ID, G.GENRE_NAME " +
                          "from FILMS_GENRES as FG join GENRES as G on FG.GENRE_ID = G.GENRE_ID " +
                          "where FG.FILM_ID = ? " +
                          "group by G.GENRE_ID";
        return new HashSet<> (jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId));
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("GENRE_ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }

    private Set<Long> getUsersLikesFromDB(long filmId) {
        String sqlQuery = "select USER_ID " +
                          "from LIKES " +
                          "where FILM_ID = ? ";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToUserId, filmId));
    }

    private long mapRowToUserId (ResultSet resultSet, long rowNum) throws SQLException {
        return resultSet.getLong("USER_ID");
    }
}
