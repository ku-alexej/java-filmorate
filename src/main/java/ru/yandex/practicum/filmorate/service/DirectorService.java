package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {

    @Autowired
    private FilmService filmService;
    private final DirectorStorage directorStorage;

    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    public Director getById(Long id) {
        validateDirectorId(id);
        return directorStorage.getById(id);
    }

    public List<Director> getByFilmId(Long filmId) {
        filmService.filmIdValidation(filmId);
        return directorStorage.getByFilmId(filmId);
    }

    public Director add(Director director) {
        validate(director);
        return directorStorage.add(director);
    }

    public Director update(Director director) {
        validate(director);
        return directorStorage.update(director);
    }

    public void delete(Long id) {
        directorStorage.delete(id);
    }

    public void updateForFilm(Long filmId, List<Director> directors) {
        directorStorage.deleteAllByFilmId(filmId);
        if (directors != null) {
            directorStorage.addAllToFilmId(filmId, directors);
        }
    }

    public static void validate(Director director) {
        if (director.getName() == null || director.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым.");
        }
    }

    public void validateDirectorId(long id) {
        log.info("Валидация данных ID режиссёра.");
        if (id <= 0) {
            throw new EntityNotFoundException("ID должен быть больше нуля.");
        }
        if (directorStorage.getById(id) == null) {
            throw new EntityNotFoundException("Режиссёр с {} " + id + " не существует.");
        }
        log.info("Валидация ID режиссёра пройдена");
    }

}
