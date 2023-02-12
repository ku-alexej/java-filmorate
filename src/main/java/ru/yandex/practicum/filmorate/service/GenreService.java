package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class GenreService {

    @Autowired
    @Qualifier("GenreDBStorage")
    private GenreStorage genreStorage;

    public List<Genre> allGenre() {
        return genreStorage.allGenres();
    }

    public Genre getGenre(int genreId) {
        return Optional.ofNullable(genreStorage.getGenre(genreId))
                .orElseThrow(() -> new EntityNotFoundException("Жанр с ID" + genreId + " не существует"));
    }

}
