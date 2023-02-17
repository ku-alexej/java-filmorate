package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
class FilmControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FilmService filmService;
    @Autowired
    @Qualifier("FilmDBStorage")
    private FilmStorage filmStorage;
    @Autowired
    private ErrorHandler errorHandler;

    private static Film film;
    private static Mpa mpa;

    @BeforeEach
    void beforeEach() {
        mpa = new Mpa(1, null);
        film = new Film(0, "name", "description",
                LocalDate.of(1895, 12, 28), 10, mpa, null, null, null);
    }

    @Test
    void addCorrectFilm() {
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), is(1L));
        assertThat(response.getBody().getName(), is("name"));
        assertThat(response.getBody().getDescription(), is("description"));
        assertThat(response.getBody().getReleaseDate(), is(LocalDate.of(1895, 12, 28)));
        assertThat(response.getBody().getDuration(), is(10));
    }

    @Test
    void addFilmWithEmptyName() {
        film.setName("");

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
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

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
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

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void addFilmWithReleaseDateBeforeFirsRelease() {

        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void addFilmWithReleaseDateEqualFirsRelease() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void addFilmWithReleaseDateAfterFirsRelease() {
        film.setReleaseDate(LocalDate.of(1895, 12, 29));

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void addFilmWithNegativeDuration() {
        film.setDuration(-1);

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void addFilmWithZeroDuration() {
        film.setDuration(0);

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

}