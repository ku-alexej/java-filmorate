package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
public class FilmService {

    private static final LocalDate FIRST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final int MAX_FILM_DESCRIPTION = 200;

    @Autowired
    @Qualifier("FilmDBStorage")
    private FilmStorage filmStorage;

    @Autowired
    private UserService userService;

    public List<Film> allFilms() {
        return filmStorage.allFilms();
    }

    public Film addFilm(Film film) {
        filmValidation(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        filmIdValidation(film.getId());
        filmValidation(film);
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(long filmId) {
        filmIdValidation(filmId);
        return filmStorage.getFilm(filmId);
    }

    public void deleteFilm(long filmId) {
        filmIdValidation(filmId);
        filmStorage.deleteFilm(filmId);
    }

    public void addLike(long filmId, long userId) {
        filmIdValidation(filmId);
        userService.userIdValidation(userId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        filmIdValidation(filmId);
        userService.userIdValidation(userId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int count, int genreId, int year) {
        if (genreId == 0 && year == 0) {
            return filmStorage.getPopular(count);
        } else if (genreId != 0 && year != 0) {
           return filmStorage.getPopularByGenreAndYear(genreId, year, count);
        } else if (genreId != 0) {
            return filmStorage.getPopularByGenre(genreId, count);
        } else {
            return filmStorage.getPopularByYear(year, count);
        }
    }

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

    public void filmIdValidation(long filmId) {
        log.info("Валидация данных ID фильма");
        if (filmId <= 0) {
            throw new EntityNotFoundException("ID должен быть больше нуля.");
        }
        if (filmStorage.getFilm(filmId) == null) {
            throw new EntityNotFoundException("Фильм с ID " + filmId + " не существует.");
        }
        log.info("Валидация ID фильма пройдена");
    }

    public List<Film> getCommonFilms(long userId, long friendId) {
        userService.userIdValidation(userId);
        userService.userIdValidation(friendId);
        return filmStorage.getCommonFilms(userId, friendId);
    }
}
