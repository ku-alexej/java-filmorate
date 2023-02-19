package ru.yandex.practicum.filmorate.storage.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FeedOperation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FeedDBStorage implements FeedStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Feed> getFeedByUserId(long userId) {
        final String sqlQuery = "SELECT EVENT_ID, USER_ID, ENTITY_ID, EVENT_TYPE, OPERATION, TIME_STAMP " +
                "FROM FEED WHERE USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFeed, userId);
    }

    private Feed mapRowToFeed(ResultSet resultSet, int rowNum) throws SQLException {
        return Feed.builder()
                .eventId(resultSet.getLong("EVENT_ID"))
                .userId(resultSet.getLong("USER_ID"))
                .entityId(resultSet.getLong("ENTITY_ID"))
                .eventType(EventType.valueOf(resultSet.getString("EVENT_TYPE")))
                .operation(FeedOperation.valueOf(resultSet.getString("OPERATION")))
                .timestamp(resultSet.getLong("TIME_STAMP"))
                .build();
    }

    @Override
    public void addToFeed(Long userId, EventType eventType, FeedOperation feedOperation, Long entityId) {
        Feed feed = Feed.builder()
                .userId(userId)
                .eventType(eventType)
                .operation(feedOperation)
                .entityId(entityId)
                .timestamp(Instant.now().toEpochMilli())
                .build();

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FEED")
                .usingGeneratedKeyColumns("EVENT_ID");
        Long idFilm = simpleJdbcInsert.executeAndReturnKey(feedToMap(feed)).longValue();
    }

    private Map<String, Object> feedToMap(Feed feed) {
        Map<String, Object> feedMap = new HashMap<>();
        feedMap.put("USER_ID", feed.getUserId());
        feedMap.put("ENTITY_ID", feed.getEntityId());
        feedMap.put("EVENT_TYPE", feed.getEventType());
        feedMap.put("OPERATION", feed.getOperation());
        feedMap.put("TIME_STAMP", feed.getTimestamp());
        return feedMap;
    }
}
