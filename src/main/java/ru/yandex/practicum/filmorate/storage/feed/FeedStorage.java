package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FeedOperation;

import java.util.List;

public interface FeedStorage {

    List<Feed> getFeedByUserId(long userId);

    void addToFeed(Long userId, EventType eventType, FeedOperation feedOperation, Long entityId);
}