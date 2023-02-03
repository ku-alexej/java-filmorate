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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("UserDBStorage")
    private UserStorage userStorage;
    @Autowired
    private ErrorHandler errorHandler;

    private User user;

    @BeforeEach
    void beforeEach() {
        user = new User(0, "mail@ya.ru", "login", "name", LocalDate.of(2000, 1, 1));
    }

    @Test
    void addCorrectUser() {
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), is(1L));
        assertThat(response.getBody().getEmail(), is("mail@ya.ru"));
        assertThat(response.getBody().getLogin(), is("login"));
        assertThat(response.getBody().getName(), is("name"));
        assertThat(response.getBody().getBirthday(), is(LocalDate.of(2000, 1, 1)));
    }

    @Test
    void addUserWithEmptyMail() {
        user.setEmail("");

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void addUserWithMailWithoutAtSign() {
        user.setEmail("mailya.ru");

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void addUserWithEmptyLogin() {
        user.setLogin("");

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void addUserWithLoginWithSpace() {
        user.setLogin("log in");

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void addUserWithoutName() {
        user.setName("");

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getName(), is("login"));
    }

    @Test
    void addUserWithBirthdayInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void addUserWithBirthdayNow() {
        user.setBirthday(LocalDate.now());

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    void addUserWithBirthdayBeforeNow() {
        user.setBirthday(LocalDate.now().minusDays(1));

        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

}