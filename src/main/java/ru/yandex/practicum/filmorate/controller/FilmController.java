package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    InMemoryFilmStorage inMemoryFilmStorage;

    public FilmController(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @GetMapping()
    public List<Film> allFilms() {
        log.info("Get /films : запрос списка фильмов");
        return inMemoryFilmStorage.allFilms();
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        log.debug("Post /films : добавление фильма {}", film);
        return inMemoryFilmStorage.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Put /films : обновление данных фильма {}", film);
        return inMemoryFilmStorage.updateFilm(film);
    }

}