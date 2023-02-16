package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
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

    public Review addReview(Review review) {
        reviewValidation(review);
        return reviewStorage.addReview(review);
    }

    public Review updateReview(Review review) {
        reviewValidation(review);
        return reviewStorage.updateReview(review);
    }

    public void deleteReview(long reviewId) {
        reviewIdValidation(reviewId);
        reviewStorage.deleteReview(reviewId);
    }

    public Review getReview(long reviewId) {
        reviewIdValidation(reviewId);
        return reviewStorage.getReview(reviewId);
    }

    public List<Review> getReviews(Long filmId, int count) {
        if (count < 0)
            throw new ValidationException("Отрицательный размер списка.");

        if (filmId == null) {
            return reviewStorage.getAll().stream()
                    .sorted(marksComparator.reversed())
                    .limit(count)
                    .collect(Collectors.toList());
        } else {
            filmService.filmIdValidation(filmId);
            return reviewStorage.getAll().stream()
                    .filter(review -> review.getFilmId().equals(filmId))
                    .sorted(marksComparator.reversed())
                    .limit(count)
                    .collect(Collectors.toList());
        }
    }

    public void changeMark(long reviewId, long userId, boolean isAdd, boolean isLike) {
        reviewIdValidation(reviewId);
        userService.userIdValidation(userId);
        if (isAdd)
            reviewStorage.addMark(reviewId, userId, isLike);
        else
            reviewStorage.deleteMark(reviewId, userId);
    }

    public void reviewValidation(Review review) {
        log.info("Валидация данных");
        if (review.getContent() == null || review.getContent().isBlank()) {
            throw new ValidationException("В отзыве нет контента");
        }
        if (review.getIsPositive() == null) {
            throw new ValidationException("У отзыва не указан тип");
        }
        if (review.getUserId() == null) {
            throw new ValidationException("У отзыва не указан пользователь");
        }
        if (review.getFilmId() == null) {
            throw new ValidationException("У отзыва не указан фильм");
        }
        userService.userIdValidation(review.getUserId());
        filmService.filmIdValidation(review.getFilmId());
        log.info("Валидация пройдена");
    }

    public void reviewIdValidation(long reviewId) {
        log.info("Валидация данных ID отзыва");
        if (reviewId <= 0 || reviewStorage.getReview(reviewId) == null) {
            throw new EntityNotFoundException("Отзыв с ID " + reviewId + " не существует.");
        }
        log.info("Валидация ID отзыва пройдена");
    }
}
