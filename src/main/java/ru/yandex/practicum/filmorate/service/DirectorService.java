package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorStorage directorStorage;

    public List<Director> getAll() {
        return new ArrayList<>(directorStorage.getAll());
    }

    public Director getById(Long id) {
        validateDirectorId(id);
        return directorStorage.getById(id);
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

    public static void validate(Director director) {
        log.info("Validation of director");
        if (director.getName() == null || director.getName().isBlank()) {
            throw new ValidationException("Director's name missing");
        }
        log.info("Validation of director passed");
    }

    public void validateDirectorId(long id) {
        log.info("Validation of director's ID");
        if (id <= 0) {
            throw new EntityNotFoundException("Director's ID must be greater than zero");
        }
        directorStorage.getById(id);
        log.info("Director's ID validation passed");
    }

}
