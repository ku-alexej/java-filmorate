package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {

    @Autowired
    @Qualifier("genreDBStorage")
    private GenreStorage genreStorage;

    public List<Genre> allGenre() {
        return genreStorage.allGenres();
    }

    public Genre getGenre(int genreId) {
        return genreStorage.getGenre(genreId);
    }

}
