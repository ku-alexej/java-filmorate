package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FeedOperation;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.feed.FeedStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    @Qualifier("UserDBStorage")
    private UserStorage userStorage;

    @Autowired
    @Qualifier("FilmDBStorage")
    private FilmStorage filmStorage;

    @Autowired
    private FeedStorage feedDBStorage;

    public List<User> allUsers() {
        return userStorage.allUsers();
    }

    public User addUser(User user) {
        userValidation(user);
        return userStorage.addUser(user);
    }

    public User getUser(long userId) {
        userIdValidation(userId);
        return userStorage.getUser(userId);
    }

    public User updateUser(User user) {
        userIdValidation(user.getId());
        userValidation(user);
        return userStorage.updateUser(user);
    }

    public void deleteUser(long userId) {
        userIdValidation(userId);
        userStorage.deleteUser(userId);
    }

    public void addFriend(long userId, long friendId) {
        userIdValidation(userId);
        userIdValidation(friendId);
        userStorage.addFriend(getUser(userId), getUser(friendId));
        feedDBStorage.addToFeed(userId, EventType.FRIEND, FeedOperation.ADD, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        userIdValidation(userId);
        userIdValidation(friendId);
        userStorage.removeFriend(userId, friendId);
        feedDBStorage.addToFeed(userId, EventType.FRIEND, FeedOperation.REMOVE, friendId);
    }

    public List<User> getUserFriends(long userId) {
        userIdValidation(userId);
        return List.copyOf(userStorage.getFriends(userId));
    }

    public List<User> mutualFriends(long userId, long otherId) {
        userIdValidation(userId);
        userIdValidation(otherId);
        List<User> userFriends = List.copyOf(userStorage.getFriends(userId));
        List<User> friendsOfFriend = List.copyOf(userStorage.getFriends(otherId));
        List<User> mutualFriends = new ArrayList<>();

        if (userFriends == null || friendsOfFriend == null) {
            return Collections.emptyList();
        }

        for (User friend : userFriends) {
            if (friendsOfFriend.contains(friend)) {
                mutualFriends.add(friend);
            }
        }

        return mutualFriends;
    }

    public List<Film> getRecommendations(long userId) {
        userIdValidation(userId);

        List<Film> recommendations = new ArrayList<>();
        User user = getUser(userId);

        List<Film> getLikedFilms = filmStorage.getLikedFilm(user.getId());

        List<User> getAllUsers = allUsers();
        getAllUsers.remove(user);

        Map<User, List<Film>> getUsersLikedFilms = getAllUsers.stream()
                .collect(Collectors.toMap(Function.identity(), u -> (filmStorage.getLikedFilm(u.getId()))));

        int maxFreq = 0;
        Map<User, Integer> similarLikeUser = new HashMap<>();

        for (Map.Entry<User, List<Film>> entry : getUsersLikedFilms.entrySet()) {
            int freq = 0;
            for (Film film : entry.getValue()) {
                if (getLikedFilms.contains(film)) {
                    freq++;
                }
            }
            if (freq > maxFreq) {
                maxFreq = freq;
            }
            similarLikeUser.put(entry.getKey(), freq);
        }

        for (Map.Entry<User, Integer> entry : similarLikeUser.entrySet()) {
            if (getUsersLikedFilms.get(entry.getKey()).size() > maxFreq) {
                List<Film> recFilms = getUsersLikedFilms.get(entry.getKey());
                recFilms.removeAll(getLikedFilms);
                recommendations.addAll(recFilms);
            }
        }

        return recommendations;
    }

    public List<Feed> getFeedByUserId(long userId) {
        userIdValidation(userId);
        return feedDBStorage.getFeedByUserId(userId);
    }

    public void userValidation(User user) {
        log.info("Валидация данных пользователя");
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
        log.info("Валидация пользователя пройдена");
    }

    public void userIdValidation(long userId) {
        log.info("Валидация данных ID пользователя");
        if (userId <= 0) {
            throw new EntityNotFoundException("ID должен быть больше нуля.");
        }
        if (userStorage.getUser(userId) == null) {
            throw new EntityNotFoundException("Пользователь с ID " + userId + " не существует.");
        }
        log.info("Валидация ID пользователя пройдена");
    }

}
