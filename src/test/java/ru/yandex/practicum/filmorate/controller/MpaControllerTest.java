package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
class MpaControllerTest {

    @Autowired
    private MpaService mpaService;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getMpaWithNegativeId() {
        ResponseEntity<Mpa> response = restTemplate.getForEntity("/mpa/-10", Mpa.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void getMpaWithZeroId() {
        ResponseEntity<Mpa> response = restTemplate.getForEntity("/mpa/0", Mpa.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void getMpaWithWrongId() {
        ResponseEntity<Mpa> response = restTemplate.getForEntity("/mpa/99", Mpa.class);

        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void getMpaWithCorrectId() {
        ResponseEntity<Mpa> response = restTemplate.getForEntity("/mpa/3", Mpa.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), is(3));
        assertThat(response.getBody().getName(), is("PG-13"));
    }
}