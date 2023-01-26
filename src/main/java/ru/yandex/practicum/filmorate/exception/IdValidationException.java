package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IdValidationException extends RuntimeException {

    public IdValidationException(final String message) {
        super(message);
        log.warn(message);
        log.warn("Валидация НЕ пройдена");
    }
}
