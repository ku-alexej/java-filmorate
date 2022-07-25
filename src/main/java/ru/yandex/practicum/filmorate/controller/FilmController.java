package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @GetMapping()
    public List<Film> allFilms() {
        log.info("Get /films : запрос списка фильмов");
        return filmService.allFilms();
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        log.debug("Post /films : добавление фильма {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Put /films : обновление данных фильма {}", film);
        return filmService.updateFilm(film);
    }
    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable long filmId) {
        log.debug("Get /films/" + filmId + " : запрос фильма");
        return filmService.getFilm(filmId);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable long filmId) {
        log.debug("Delete /films/" + filmId + " : удаление данных фильма");
        filmService.deleteFilm(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void likeFilm(@PathVariable long filmId, @PathVariable long userId) {
        log.debug("Put /films/" + filmId + "/like/" + userId + " : лайк фильму");
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable long filmId, @PathVariable long userId) {
        log.debug("Delete /films/" + filmId + "/like/" + userId + " : снять лайк фильму");
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> mostLiked(@RequestParam(required = false) String count) {
        log.debug("Get /films/popular : запрос " + count + " популярных фильмов");
        if (count == null) {
            return filmService.getPopular(10);
        }
        return filmService.getPopular(Long.parseLong(count));
    }

}