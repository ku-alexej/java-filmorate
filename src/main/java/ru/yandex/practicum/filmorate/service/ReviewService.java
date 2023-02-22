package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.FeedOperation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReviewService {

    private static final Comparator<Review> marksComparator = Comparator.comparing(Review::getUseful);

    @Autowired
    private ReviewStorage reviewStorage;

    @Autowired
    private UserService userService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private FeedStorage feedDBStorage;

    public Review createReview(Review review) {
        reviewValidation(review);
        Review newReview = reviewStorage.createReview(review);
        feedDBStorage.addToFeed(newReview.getUserId(), EventType.REVIEW, FeedOperation.ADD, newReview.getReviewId());
        return newReview;
    }

    public Review updateReview(Review review) {
        reviewValidation(review);
        Review reviewFromDB = getReview(review.getReviewId());
        feedDBStorage.addToFeed(reviewFromDB.getUserId(), EventType.REVIEW, FeedOperation.UPDATE, review.getReviewId());
        return reviewStorage.updateReview(review);
    }

    public void deleteReview(long reviewId) {
        reviewIdValidation(reviewId);
        Review review = getReview(reviewId);
        reviewStorage.deleteReview(reviewId);
        feedDBStorage.addToFeed(review.getUserId(), EventType.REVIEW, FeedOperation.REMOVE, reviewId);
    }

    public Review getReview(long reviewId) {
        reviewIdValidation(reviewId);
        return reviewStorage.getReview(reviewId);
    }

    public List<Review> getReviews(Long filmId, int count) {
        if (count < 0) {
            throw new ValidationException("Count of reviews cannot be negative");
        }
        return reviewStorage.getReviews(filmId, count).stream()
                .sorted(marksComparator.reversed())
                .collect(Collectors.toList());
    }

    public void changeMark(long reviewId, long userId, boolean isAdd, boolean isLike) {
        reviewIdValidation(reviewId);
        userService.userIdValidation(userId);
        if (isAdd) {
            reviewStorage.addMark(reviewId, userId, isLike);
        } else {
            reviewStorage.deleteMark(reviewId, userId);
        }
    }

    private void reviewValidation(Review review) {
        log.info("Validation of review");
        if (review.getContent() == null || review.getContent().isBlank()) {
            throw new ValidationException("Review's content cannot be empty");
        }
        if (review.getIsPositive() == null) {
            throw new ValidationException("Review's type missing");
        }
        if (review.getUserId() == null) {
            throw new ValidationException("Review's user missing");
        }
        if (review.getFilmId() == null) {
            throw new ValidationException("Review's film missing");
        }
        userService.userIdValidation(review.getUserId());
        filmService.filmIdValidation(review.getFilmId());
        log.info("Validation of review passed");
    }

    private void reviewIdValidation(long reviewId) {
        log.info("Validation of review's ID");
        if (reviewId <= 0) {
            throw new EntityNotFoundException("Review's ID must be greater than zero");
        }
        log.info("Review's ID validation passed");
    }
}
