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

    @GetMapping
    public List<Genre> allGenre() {
        log.info("GET /genres : get list of all genres");
        return genreService.allGenre();
    }

    @GetMapping("/{genreId}")
    public Genre getGenre(@PathVariable int genreId) {
        log.debug("GET /genres/{} : get genre by ID", genreId);
        return genreService.getGenre(genreId);
    }
}
