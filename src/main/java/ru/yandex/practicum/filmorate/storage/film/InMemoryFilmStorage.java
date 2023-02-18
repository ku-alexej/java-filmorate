package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Qualifier("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Long, Film> films = new HashMap<>();
    private long filmId = 1;

    @Override
    public List<Film> allFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(filmId);
        log.debug("Новому фильму присвоен ID: {}", film.getId());
        films.put(filmId, film);
        log.debug("Фильм сохранен, в базе {} фильмов", films.size());
        filmId++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
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
    public void deleteFilm(long filmId) {
        films.remove(filmId);
    }

    @Override
    public void addLike(long filmId, long userId) {
        films.get(filmId).getUsersId().add(userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        films.get(filmId).getUsersId().remove(userId);
    }

    @Override
    public List<Film> getPopular(long count) {
        return null;
    }

    @Override
    public Film getFilm(long filmId) {
        return films.get(filmId);
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        return null;
    }

    @Override
    public List<Film> getLikedFilm(long userId) {
        return null;
    }

    @Override
    public List<Film> allDirectorsFilms(long directorId) {
        return null;
    }

}