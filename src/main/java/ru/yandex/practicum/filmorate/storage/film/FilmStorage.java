package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

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
}
