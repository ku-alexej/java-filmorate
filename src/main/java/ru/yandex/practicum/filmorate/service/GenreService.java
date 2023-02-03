package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

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
        genreIdValidation(genreId);
        return genreStorage.getGenre(genreId);
    }

    private void genreIdValidation(int genreId) {
        log.info("Валидация данных ID жанра");
        if (genreId <= 0) {
            throw new IdValidationException("ID должен быть больше нуля.");
        }
        if (genreStorage.getGenre(genreId) == null) {
            throw new IdValidationException("Жанр с ID " + genreId + " не существует.");
        }
        log.info("Валидация ID жанра пройдена");
    }
}
