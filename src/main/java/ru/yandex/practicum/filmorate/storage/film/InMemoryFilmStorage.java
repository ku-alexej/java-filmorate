package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();
    private long filmId = 1;
    private static final LocalDate FIRST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final int MAX_FILM_DESCRIPTION = 200;

    @Override
    public List<Film> allFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        filmValidation(film);
        film.setId(filmId);
        log.debug("Новому фильму присвоен ID: {}", film.getId());
        films.put(filmId, film);
        log.debug("Фильм сохранен, в базе {} фильмов", films.size());
        filmId++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidation(film);
        Film updatedFilm;
        if (films.containsKey(film.getId())) {
            updatedFilm = changeFilm(film);
        } else {
            log.warn("Фильм не найден");
            throw new ValidationException("Фильм не найден.");
        }
        return updatedFilm;
    }

    @Override
    public Film changeFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Данные фильма обновлены");
        return film;
    }

    @Override
    public void filmValidation(Film film) {
        log.info("Валидация данных");
        if (film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > MAX_FILM_DESCRIPTION) {
            throw new ValidationException("Описание фильма больше " + MAX_FILM_DESCRIPTION + " символов.");
        }
        if (film.getReleaseDate().isBefore(FIRST_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше " + FIRST_RELEASE_DATE + ".");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной.");
        }
        log.info("Валидация пройдена");
    }
}
