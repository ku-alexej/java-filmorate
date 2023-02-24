package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Feed {
    private Long eventId;
    private Long userId;
    private Long entityId;
    private EventType eventType;
    private FeedOperation operation;
    private Long timestamp;
}

