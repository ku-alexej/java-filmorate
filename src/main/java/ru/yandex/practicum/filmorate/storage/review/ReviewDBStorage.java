package ru.yandex.practicum.filmorate.storage.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class ReviewDBStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Review> getAll() {
        final String sqlQuery = "select REVIEW_ID, CONTENT, IS_POSITIVE, USER_ID, FILM_ID " +
                "from REVIEWS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToReview);
    }

    @Override
    public Review addReview(Review review) {
        final String sqlQuery = "insert into REVIEWS (CONTENT, IS_POSITIVE, USER_ID, FILM_ID) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"REVIEW_ID"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setLong(3, review.getUserId());
            stmt.setLong(4, review.getFilmId());
            return stmt;
        }, keyHolder);
        review.setReviewId(keyHolder.getKey().longValue());
        log.debug("Новому отзыву присвоен ID: {}", review.getReviewId());
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        log.debug("Обновлен отзыва с ID: {}", review.getReviewId());
        final String sqlQuery = "update REVIEWS set " +
                "CONTENT = ?, IS_POSITIVE = ?" +
                "where REVIEW_ID = ?";

        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());

        return getReview(review.getReviewId());
    }

    @Override
    public void deleteReview(long reviewId) {
        String sqlQuery = "delete from REVIEWS " +
                "where REVIEW_ID = ?";
        jdbcTemplate.update(sqlQuery, reviewId);
        log.debug("Удален отзыв с ID: {}", reviewId);
    }

    @Override
    public Review getReview(long reviewId) {
        String sqlQuery = "select REVIEW_ID, CONTENT, IS_POSITIVE, USER_ID, FILM_ID " +
                "from REVIEWS " +
                "where REVIEW_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToReview, reviewId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void addMark(long reviewId, long userId, boolean isLike) {
        final String sqlQuery = "insert into REVIEWS_LIKES (REVIEW_ID, USER_ID, MARK) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"REVIEWS_LIKE_ID"});
            stmt.setLong(1, reviewId);
            stmt.setLong(2, userId);
            if (isLike)
                stmt.setLong(3, 1);
            else
                stmt.setLong(3, -1);
            return stmt;
        }, keyHolder);
        log.debug("Поставлена оценка отзыву ID {} от пользователя ID {}", reviewId, userId);
    }

    @Override
    public void deleteMark(long reviewId, long userId) {
        String sqlQuery = "delete from REVIEWS_LIKES " +
                "where REVIEW_ID = ? and USER_ID = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId);
        log.debug("Удалена оценка отзыву ID {} от пользователя ID {}", reviewId, userId);
    }

    private Review mapRowToReview(ResultSet resultSet, int rowNum) throws SQLException {
        return Review.builder()
                .reviewId(resultSet.getLong("REVIEW_ID"))
                .content(resultSet.getString("CONTENT"))
                .isPositive(resultSet.getBoolean("IS_POSITIVE"))
                .userId(resultSet.getLong("USER_ID"))
                .filmId(resultSet.getLong("FILM_ID"))
                .useful(calcUseful(resultSet.getLong("REVIEW_ID")))
                .build();
    }

    private long calcUseful(long reviewId) {
        Long count;
        String sqlQuery = "select sum(MARK) " +
                "from REVIEWS_LIKES " +
                "where REVIEW_ID = ?";

        count = jdbcTemplate.queryForObject(sqlQuery, new Object[]{reviewId}, Long.class);
        if (count == null)
            return 0;
        return count;
    }

}