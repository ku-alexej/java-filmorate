package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping()
    public Review addReview(@RequestBody Review review) {
        log.info("Post /reviews : добавление отзыва {}", review);
        return reviewService.addReview(review);
    }

    @PutMapping()
    public Review updateReview(@RequestBody Review review) {
        log.info("Put /reviews/ : обновление отзыва {}", review);
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{reviewId}")
    public void removeReview(@PathVariable long reviewId) {
        log.info("Delete /reviews/" + reviewId + " : удаление отзыва {}", reviewId);
        reviewService.deleteReview(reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review getReview(@PathVariable long reviewId) {
        log.info("Get /reviews/" + reviewId + " : запрос отзыва {}", reviewId);
        return reviewService.getReview(reviewId);
    }

    @GetMapping()
    public List<Review> getReviews(@RequestParam(required = false) Long filmId,
                                   @RequestParam(defaultValue = "10", required = false) int count) {
        log.info("Get /reviews/ : запрос списка отзывов");
        return reviewService.getReviews(filmId, count);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@PathVariable long reviewId, @PathVariable long userId) {
        log.info("Put /reviews/" + reviewId + "/like/" + userId +
                " : лайк отзыву {} от пользователя {}", reviewId, userId);
        reviewService.changeMark(reviewId, userId, true, true);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDisLike(@PathVariable long reviewId, @PathVariable long userId) {
        log.info("Put /reviews/" + reviewId + "/dislike/" + userId +
                " : дизлайк отзыву {} от пользователя {}", reviewId, userId);
        reviewService.changeMark(reviewId, userId, true, false);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeLike(@PathVariable long reviewId, @PathVariable long userId) {
        log.info("Delete /reviews/" + reviewId + "/like/" + userId +
                " : удаление лайка отзыву {} от пользователя {}", reviewId, userId);
        reviewService.changeMark(reviewId, userId, false, true);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeDislike(@PathVariable long reviewId, @PathVariable long userId) {
        log.info("Delete /reviews/" + reviewId + "/dislike/" + userId +
                " : удаление дизлайка отзыву {} от пользователя {}", reviewId, userId);
        reviewService.changeMark(reviewId, userId, false, false);
    }

}