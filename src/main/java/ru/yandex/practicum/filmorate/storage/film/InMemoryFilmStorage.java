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
@Qualifier("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long filmId = 1;

    @Override
    public List<Film> allFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(filmId);
        log.debug("New film got ID: {}", film.getId());
        films.put(filmId, film);
        log.debug("{} film(s) in memory", films.size());
        filmId++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Film updatedFilm;
        if (films.containsKey(film.getId())) {
            updatedFilm = changeFilm(film);
        } else {
            throw new ValidationException("Film with ID " + film.getId() + " does not exist");
        }
        return updatedFilm;
    }

    @Override
    public Film changeFilm(Film film) {
        films.put(film.getId(), film);
        log.debug("Film with ID {} was updated", film.getId());
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
        return List.of();
    }

    @Override
    public Film getFilm(long filmId) {
        return films.get(filmId);
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        return List.of();
    }

    @Override
    public List<Film> getLikedFilm(long userId) {
        return List.of();
    }

    @Override
    public List<Film> getPopularByGenre(int genreId, int count) {
        return List.of();
    }

    @Override
    public List<Film> getPopularByYear(int year, int count) {
        return List.of();
    }

    @Override
    public List<Film> getPopularByGenreAndYear(int genreId, int year, int count) {
        return List.of();
    }

    @Override
    public List<Film> allDirectorsFilms(long directorId) {
        return List.of();
    }

    @Override
    public List<Film> searchFilm(String searchQuery, boolean searchByName, boolean searchByDirector) {
        return List.of();
    }

}