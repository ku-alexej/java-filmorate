package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

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
    public List<Film> mostLiked(@RequestParam(defaultValue = "10") int count,
                                @RequestParam(defaultValue = "0") int genreId,
                                @RequestParam(defaultValue = "0") int year) {
        log.debug("Get /films/popular : запрос " + count + " популярных фильмов по жанру "
                + genreId + "  и году " + year);
        return filmService.getPopular(count, genreId, year);
    }

    @GetMapping("/search")
    public List<Film> searchFilm(@RequestParam("query") Optional<String> query, @RequestParam("by") Optional<String> searchBy){
        if(!query.isEmpty()){
            log.info("поиск по запросу: {}", query);
            if(searchBy.isPresent())
                return filmService.searchFilm(query.get(), searchBy.get());
            else
                return filmService.searchFilm(query.get(), "title,director");
        } else
            throw new EntityNotFoundException("отсутствует строка поиска");
    }
    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam long userId, @RequestParam long friendId) {
        log.debug("Get /films/common?userId={userId}&friendId={friendId} " +
                ": запрос общих фильмов у пользователей с id " +
                userId + " и " + friendId);
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorsFilmSorted(@PathVariable long directorId, @RequestParam String sortBy) {
        log.debug("GET /films/director/{directorId}?sortBy=[year,likes]");
        return filmService.getDirectorsFilmSorted(directorId, sortBy);
    }
}