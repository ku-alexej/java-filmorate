package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    List<Review> getReviews(Long filmId, int count);

    Review createReview(Review review);

    Review updateReview(Review review);

    void deleteReview(long reviewId);

    Review getReview(long reviewId);

    void addMark(long reviewId, long userId, boolean isLike);

    void deleteMark(long reviewId, long userId);

}