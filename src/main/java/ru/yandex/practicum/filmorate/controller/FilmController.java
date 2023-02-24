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
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortTypes;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @GetMapping
    public List<Film> allFilms() {
        log.info("GET /films : get list of all films");
        return filmService.allFilms();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.debug("POST /films : create film - {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("PUT /films : update film - {}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable long filmId) {
        log.debug("GET /films/{} : get film by ID", filmId);
        return filmService.getFilm(filmId);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable long filmId) {
        log.debug("DELETE /films/{} : delete film by ID", filmId);
        filmService.deleteFilm(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void likeFilm(@PathVariable long filmId, @PathVariable long userId) {
        log.debug("PUT /films/{}/like/{} : create like for film from user", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable long filmId, @PathVariable long userId) {
        log.debug("DELETE /films/{}/like/{} : delete like for film from user", filmId, userId);
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> mostLiked(@RequestParam(defaultValue = "10", required = false) int count,
                                @RequestParam(defaultValue = "0", required = false) int genreId,
                                @RequestParam(defaultValue = "0", required = false) int year) {
        log.debug("GET /films/popular?count={}&genreId={}&year={} : " +
                "get list of popular films by genre and/or year", count, genreId, year);
        return filmService.getPopular(count, genreId, year);
    }

    @GetMapping("/search")
    public List<Film> searchFilm(@RequestParam(name = "query", required = false) String query,
                                 @RequestParam(name = "by", defaultValue = "title,director", required = false)
                                 String searchBy) {
        log.info("GET /films/search?query={}&by={} : get films by search", query, searchBy);
        if (query == null) {
            throw new EntityNotFoundException("Missing search query");
        }
        return filmService.searchFilm(query, searchBy);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam long userId, @RequestParam long friendId) {
        log.debug("GET /films/common?userId={}&friendId={} : " +
                "get list of common films of user and friend", userId, friendId);
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorsFilmSorted(@PathVariable long directorId, @RequestParam String sortBy) {
        log.debug("GET /films/director/{}?sortBy={} : get list of films of director", directorId, sortBy);
        return filmService.getDirectorsFilmSorted(directorId, Enum.valueOf(SortTypes.class, sortBy.toUpperCase()));
    }
}