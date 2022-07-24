package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public List<User> allUsers() {
        log.info("Get /users : запрос списка пользователей");
        return userService.allUsers();
    }

    @GetMapping("/{userId}")
    public User getUsers(@PathVariable long userId) {
        log.debug("Get /users/" + userId + " : запрос данных пользователя");
        return userService.getUser(userId);
    }

    @PostMapping()
    public User addUser(@RequestBody User user) {
        log.debug("Post /users : добавление пользователя {}", user);
        return userService.addUser(user);
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {
        log.debug("Put /users : обновление данных пользователя {}", user);
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.debug("Put /users/" + userId + "/friends/" + friendId + " : добавление в друзья");
        userService.addFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable long userId) {
        log.debug("Get /users/" + userId + " : получение списка друзей");
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List getMutualFriends(@PathVariable long userId, @PathVariable long otherId) {
        log.debug("Get /users/" + userId + "/friends/common/" + otherId + " : получение общих друзей");
        return userService.mutualFriends(userId, otherId);
    }

    @DeleteMapping("/{userID}")
    public long deleteUser(@PathVariable long userId) {
        log.debug("Delete /users/" + userId + " : удаление данных пользователя");
        return userService.deleteUser(userId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.debug("Post /users/" + userId + "/friends/" + friendId + " : удаление из друзей");
        userService.removeFriend(userId, friendId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> incorrectUserId(final IdValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationFail(final ValidationException e) {
        return Map.of("error", e.getMessage());
    }

}