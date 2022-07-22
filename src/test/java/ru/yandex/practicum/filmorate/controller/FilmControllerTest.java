package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private static FilmController filmController;
    private static Film film;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        film = new Film(1, "name", "description", LocalDate.of(1895, 12, 28), 10);
    }

    @Test
    void addCorrectFilm() {
        filmController.addFilm(film);

        assertEquals(1, filmController.allFilms().size(), "Фильм не добавлен.");
    }

    @Test
    void addFilmWithEmptyName() {
        film.setName("");

        try {
            filmController.addFilm(film);
            fail("Не ловит исключение в названии.");
            }
        catch (Exception e) {
            final String expected = "Название фильма не может быть пустым.";
            assertEquals(expected, e.getMessage(), "Не верное исключение.");
        }
    }

    @Test
    void addFilmWithMaxDescription() {
        String symbols = "0123456789" + "0123456789" +
                        "0123456789" + "0123456789" +
                        "0123456789" + "0123456789" +
                        "0123456789" + "0123456789" +
                        "0123456789" + "0123456789" +
                        "0123456789" + "0123456789" +
                        "0123456789" + "0123456789" +
                        "0123456789" + "0123456789" +
                        "0123456789" + "0123456789" +
                        "0123456789" + "012345678";

        film.setDescription(symbols);
        filmController.addFilm(film);

        assertEquals(1, filmController.allFilms().size(), "Фильм не добавлен.");
    }

    @Test
    void addFilmWithExceedDescription() {
        String symbols = "0123456789" + "0123456789" +
                "0123456789" + "0123456789" +
                "0123456789" + "0123456789" +
                "0123456789" + "0123456789" +
                "0123456789" + "0123456789" +
                "0123456789" + "0123456789" +
                "0123456789" + "0123456789" +
                "0123456789" + "0123456789" +
                "0123456789" + "0123456789" +
                "0123456789" + "0123456789" +
                "0";

        film.setDescription(symbols);

        try {
            filmController.addFilm(film);
            fail("Не ловит исключение в описании.");
        }
        catch (Exception e) {
            final String expected = "Описание фильма больше 200 символов.";
            assertEquals(expected, e.getMessage(), "Не верное исключение.");
        }
    }

    @Test
    void addFilmWithReleaseDateBeforeFirsRelease() {

        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        try {
            filmController.addFilm(film);
            fail("Не ловит исключение в дате релиза.");
        }
        catch (Exception e) {
            final String expected = "Дата релиза не может быть раньше " + LocalDate.of(1895, 12, 28) + ".";
            assertEquals(expected, e.getMessage(), "Не верное исключение.");
        }
    }

    @Test
    void addFilmWithReleaseDateEqualFirsRelease() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmController.addFilm(film);

        assertEquals(1, filmController.allFilms().size(), "Фильм не добавлен.");
    }

    @Test
    void addFilmWithReleaseDateAfterFirsRelease() {
        film.setReleaseDate(LocalDate.of(1895, 12, 29));
        filmController.addFilm(film);

        assertEquals(1, filmController.allFilms().size(), "Фильм не добавлен.");
    }

    @Test
    void addFilmWithNegativeDuration() {
        film.setDuration(-1);

        try {
            filmController.addFilm(film);
            fail("Не ловит исключение в продолжительности.");
        }
        catch (Exception e) {
            final String expected = "Продолжительность фильма не может быть отрицательной.";
            assertEquals(expected, e.getMessage(), "Не верное исключение.");
        }
    }

    @Test
    void addFilmWithZeroDuration() {
        film.setDuration(0);
        filmController.addFilm(film);

        assertEquals(1, filmController.allFilms().size(), "Фильм не добавлен.");
    }

}