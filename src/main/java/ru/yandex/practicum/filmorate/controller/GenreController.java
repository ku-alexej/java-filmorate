package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping()
    public List<Genre> allGenre() {
        log.info("Get /genres : запрос списка жанров");
        return genreService.allGenre();
    }

    @GetMapping("/{genreId}")
    public Genre getGenre(@PathVariable int genreId) {
        log.debug("Get /genres/" + genreId + " : запрос жанра");
        return genreService.getGenre(genreId);
    }
}
