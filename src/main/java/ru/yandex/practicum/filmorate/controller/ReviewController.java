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

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        log.info("POST /reviews : create review - {}", review);
        return reviewService.createReview(review);
    }

    @PutMapping
    public Review updateReview(@RequestBody Review review) {
        log.info("PUT /reviews/ : update review - {}", review);
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{reviewId}")
    public void removeReview(@PathVariable long reviewId) {
        log.info("DELETE /reviews/{} : delete review by ID", reviewId);
        reviewService.deleteReview(reviewId);
    }

    @GetMapping("/{reviewId}")
    public Review getReview(@PathVariable long reviewId) {
        log.info("GET /reviews/{} : get review by ID", reviewId);
        return reviewService.getReview(reviewId);
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam(required = false) Long filmId,
                                   @RequestParam(defaultValue = "10", required = false) int count) {
        log.info("GET /reviews?filmId={}&count={} : get list of reviews", filmId, count);
        return reviewService.getReviews(filmId, count);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@PathVariable long reviewId, @PathVariable long userId) {
        log.info("PUT /reviews/{}/like/{} : add like to review from user", reviewId, userId);
        reviewService.changeMark(reviewId, userId, true, true);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDisLike(@PathVariable long reviewId, @PathVariable long userId) {
        log.info("PUT /reviews/{}/dislike/{} : add dislike to review from user", reviewId, userId);
        reviewService.changeMark(reviewId, userId, true, false);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeLike(@PathVariable long reviewId, @PathVariable long userId) {
        log.info("DELETE /reviews/{}/like/{} : remove like for review from user", reviewId, userId);
        reviewService.changeMark(reviewId, userId, false, true);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeDislike(@PathVariable long reviewId, @PathVariable long userId) {
        log.info("DELETE /reviews/{}/dislike/{} : remove dislike for review from user", reviewId, userId);
        reviewService.changeMark(reviewId, userId, false, false);
    }

}