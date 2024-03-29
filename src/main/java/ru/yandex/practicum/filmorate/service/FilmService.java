package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.FeedOperation;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortTypes;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FilmService {

    private static final LocalDate FIRST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final int MAX_FILM_DESCRIPTION = 200;
    private static final Comparator<Film> likesComparator = Comparator.comparing(obj -> obj.getUsersId().size());
    private static final Comparator<Film> releaseDateComparator = Comparator.comparing(obj -> obj.getReleaseDate());


    @Autowired
    @Qualifier("filmDBStorage")
    private FilmStorage filmStorage;

    @Autowired
    private UserService userService;

    @Autowired
    private DirectorService directorService;

    @Autowired
    private FeedStorage feedDBStorage;


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
        feedDBStorage.addToFeed(userId, EventType.LIKE, FeedOperation.ADD, filmId);
    }

    public void removeLike(long filmId, long userId) {
        filmIdValidation(filmId);
        userService.userIdValidation(userId);
        filmStorage.removeLike(filmId, userId);
        feedDBStorage.addToFeed(userId, EventType.LIKE, FeedOperation.REMOVE, filmId);
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

    public List<Film> searchFilm(String query, String searchBy) {
        boolean searchByName, searchByDirector;
        searchByName = searchBy.toLowerCase().contains("title");
        searchByDirector = searchBy.toLowerCase().contains("director");
        return filmStorage.searchFilm(query, searchByName, searchByDirector);
    }

    public void filmValidation(Film film) {
        log.info("Validation of film");
        if (film.getName().isBlank()) {
            throw new ValidationException("Film's name missing");
        }
        if (film.getDescription().length() > MAX_FILM_DESCRIPTION) {
            throw new ValidationException("Film's description is longer than " + MAX_FILM_DESCRIPTION + " characters");
        }
        if (film.getReleaseDate().isBefore(FIRST_RELEASE_DATE)) {
            throw new ValidationException("Film's release date cannot be earlier than " + FIRST_RELEASE_DATE);
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Film's duration cannot be negative");
        }
        log.info("Validation of film passed");
    }

    public void filmIdValidation(long filmId) {
        log.info("Validation of film ID");
        if (filmId <= 0) {
            throw new EntityNotFoundException("Film's ID must be greater than zero");
        }
        filmStorage.getFilm(filmId);
        log.info("Film's ID validation passed");
    }

    public List<Film> getCommonFilms(long userId, long friendId) {
        userService.userIdValidation(userId);
        userService.userIdValidation(friendId);
        return filmStorage.getCommonFilms(userId, friendId);
    }

    public List<Film> getDirectorsFilmSorted(long directorId, SortTypes sortBy) {
        directorService.validateDirectorId(directorId);
        switch (sortBy) {
            case LIKES:
                return filmStorage.allDirectorsFilms(directorId).stream()
                        .sorted(likesComparator.reversed())
                        .collect(Collectors.toList());
            case YEAR:
                return filmStorage.allDirectorsFilms(directorId).stream()
                        .sorted(releaseDateComparator)
                        .collect(Collectors.toList());
            default:
                throw new EntityNotFoundException("Sort type does not exist or missing");
        }
    }
}
