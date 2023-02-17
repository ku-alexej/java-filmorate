package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public interface FilmStorage {

    List<Film> allFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film changeFilm(Film film);

    void deleteFilm(long filmId);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Film> getPopular(long count);

    Film getFilm(long userId);

    List<Film> getCommonFilms(long userId, long friendId);

    List<Film> getLikedFilm(long userId);

    List<Film> allDirectorsFilms(long directorId);
}
