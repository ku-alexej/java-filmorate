package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private HashMap<Long, User> users = new HashMap<>();
    private long userId = 1;

    @GetMapping()
    public ArrayList<User> allUsers() {
        log.info("Get /users : запрос списка пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User addUser(@RequestBody User user) {
        log.debug("Post /users : добавление пользователя {}", user);
        userValidation(user);
        return saveUser(user);
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {
        log.debug("Put /users : обновление данных пользователя {}", user);
        userValidation(user);
        User updatedUser;
        if (users.containsKey(user.getId())) {
            updatedUser = changeUser(user);
        } else {
            log.warn("Пользователь не найден");
            throw new ValidationException("Пользователь не найден.");
        }
        return updatedUser;
    }

    private User saveUser(User user) {
        user.setId(userId);
        log.debug("Новому пользователю присвоен ID: {}", user.getId());
        users.put(userId, user);
        log.debug("Пользователь сохранен, в базе {} пользователей", users.size());
        userId++;
        return user;
    }

    private User changeUser(User user) {
        users.put(user.getId(), user);
        log.info("Данные пользователя обновлены");
        return user;
    }

    private void userValidation (User user) {
        log.info("Валидация данных");
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Почта не может быть пустой и должна содержать символ «@».");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и не может содержать пробелы.");
        }
        if (user.getName().isBlank()) {
            log.info("Имя пользователя пустое");
            user.setName(user.getLogin());
            log.info("В качестве имени установлено значение логина");

        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        log.info("Валидация пройдена");
    }
}
