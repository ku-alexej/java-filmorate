package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> getAll();

    Director getById(Long id);

    Director add(Director director);

    Director update(Director director);

    void delete(Long id);

}

