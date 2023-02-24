package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> allUsers() {
        log.info("GET /users : get list of all users");
        return userService.allUsers();
    }

    @GetMapping("/{userId}")
    public User getUsers(@PathVariable long userId) {
        log.debug("GET /users/{} : get user by ID", userId);
        return userService.getUser(userId);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.debug("POST /users : create user - {}", user);
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("PUT /users : update user - {}", user);
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.debug("PUT /users/{}/friends/{} : user add friend", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable long userId) {
        log.debug("GET /users/{}/friends : get list of user's friends", userId);
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable long userId, @PathVariable long otherId) {
        log.debug("GET /users/{}/friends/common/{} : get list of common friends", userId, otherId);
        return userService.mutualFriends(userId, otherId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.debug("DELETE /users/{} : delete user by ID", userId);
        userService.deleteUser(userId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable long userId, @PathVariable long friendId) {
        log.debug("DELETE /users/{}/friends/{} : delete friend", userId, friendId);
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/recommendations")
    public List<Film> getRecommendations(@PathVariable long userId) {
        log.debug("GET /users/{}/recommendations : get list of films recommended by user", userId);
        return userService.getRecommendations(userId);
    }

    @GetMapping("/{userId}/feed")
    public List<Feed> getFeedByUserId(@PathVariable long userId) {
        log.debug("GET /users/{}/feed : get list of feeds of user", userId);
        return userService.getFeedByUserId(userId);
    }
}