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
    FilmService filmService;

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
    public List mostLiked(@RequestParam long count) {
        log.debug("Get /films/popular : запрос " + count + " популярных фильмов");
        return filmService.getPopular(count);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> incorrectFilmId(final IdValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationFail(final ValidationException e) {
        return Map.of("error", e.getMessage());
    }
}