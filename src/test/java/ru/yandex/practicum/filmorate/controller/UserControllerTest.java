package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private static UserController userController;
    private static User user;

//    @BeforeEach
//    void beforeEach() {
//        userController = new UserController(new InMemoryUserStorage());
//        user = new User(1, "mail@ya.ru", "login", "name", LocalDate.of(2000, 1, 1));
//    }

    @Test
    void addCorrectUser() {
        userController.addUser(user);

        assertEquals(1, userController.allUsers().size(), "Пользователь не добавлен.");
    }

    @Test
    void addUserWithEmptyMail() {
        user.setEmail("");

        try {
            userController.addUser(user);
            fail("Не ловит исключение в почте.");
        }
        catch (Exception e) {
            final String expected = "Почта не может быть пустой и должна содержать символ «@».";
            assertEquals(expected, e.getMessage(), "Не верное исключение.");
        }
    }

    @Test
    void addUserWithMailWithoutAtSign() {
        user.setEmail("mailya.ru");

        try {
            userController.addUser(user);
            fail("Не ловит исключение в почте.");
        }
        catch (Exception e) {
            final String expected = "Почта не может быть пустой и должна содержать символ «@».";
            assertEquals(expected, e.getMessage(), "Не верное исключение.");
        }
    }

    @Test
    void addUserWithEmptyLogin() {
        user.setLogin("");

        try {
            userController.addUser(user);
            fail("Не ловит исключение в логине.");
        }
        catch (Exception e) {
            final String expected = "Логин не может быть пустым и не может содержать пробелы.";
            assertEquals(expected, e.getMessage(), "Не верное исключение.");
        }
    }

    @Test
    void addUserWithLoginWithSopace() {
        user.setLogin("log in");

        try {
            userController.addUser(user);
            fail("Не ловит исключение в логине.");
        }
        catch (Exception e) {
            final String expected = "Логин не может быть пустым и не может содержать пробелы.";
            assertEquals(expected, e.getMessage(), "Не верное исключение.");
        }
    }

    @Test
    void addUserWithoutName() {
        user.setName("");
        userController.addUser(user);

        assertEquals(user.getLogin(), user.getName(), "Не заменяет пустое имя на логин.");
    }

    @Test
    void addUserWithBirthdayInFuture() {
        user.setBirthday(LocalDate.now().plusDays(1));

        try {
            userController.addUser(user);
            fail("Не ловит исключение в дате рождения.");
        }
        catch (Exception e) {
            final String expected = "Дата рождения не может быть в будущем.";
            assertEquals(expected, e.getMessage(), "Не верное исключение.");
        }
    }

    @Test
    void addUserWithBirthdayNow() {
        user.setBirthday(LocalDate.now());
        userController.addUser(user);

        assertEquals(1, userController.allUsers().size(), "Пользователь не добавлен.");
    }

    @Test
    void addUserWithBirthdayBeforeNow() {
        user.setBirthday(LocalDate.now().minusDays(1));
        userController.addUser(user);

        assertEquals(1, userController.allUsers().size(), "Пользователь не добавлен.");
    }

}