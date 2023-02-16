package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private long userId = 1;

    @Override
    public List<User> allUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        user.setId(userId);
        log.debug("Новому пользователю присвоен ID: {}", user.getId());
        users.put(userId, user);
        log.debug("Пользователь сохранен, в базе {} пользователей", users.size());
        userId++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        User updatedUser;
        if (users.containsKey(user.getId())) {
            updatedUser = changeUser(user);
        } else {
            log.warn("Пользователь не найден");
            throw new ValidationException("Пользователь не найден.");
        }
        return updatedUser;
    }

    @Override
    public void deleteUser(long userId) {
        users.remove(userId);
    }

    @Override
    public void addFriend(User user, User friend) {
        user.getFriendsId().add(friend.getId());
        friend.getFriendsId().add(user.getId());
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        if (users.containsKey(userId) && users.containsKey(friendId)) {
            users.get(userId).getFriendsId().remove(friendId);
            users.get(friendId).getFriendsId().remove(userId);
        } else {
            throw new ValidationException("Проверьте ID пользователя и друга.");
        }
    }

    @Override
    public List<User> getFriends(long userId) {
        User user = users.get(userId);
        List<User> listOfFriends = new ArrayList<>();
        for (long friendId : user.getFriendsId()) {
            listOfFriends.add(users.get(friendId));
        }

        return listOfFriends;
    }

    @Override
    public User getUser(long userId) {
        return users.get(userId);
    }

    @Override
    public User changeUser(User user) {
        users.put(user.getId(), user);
        log.info("Данные пользователя обновлены");
        return user;
    }

}
