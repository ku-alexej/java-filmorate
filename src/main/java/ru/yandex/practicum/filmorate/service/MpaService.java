package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {

    @Autowired
    @Qualifier("MpaDBStorage")
    private MpaStorage mpaStorage;

    public List<Mpa> allMpa() {
        return mpaStorage.allMpa();
    }

    public Mpa getMpa(int mpaId) {
        mpaIdValidation(mpaId);
        return mpaStorage.getMpa(mpaId);
    }

    private void mpaIdValidation(int mpaId) {
        log.info("Валидация данных ID mpa");
        if (mpaId <= 0) {
            throw new IdValidationException("ID должен быть больше нуля.");
        }
        if (mpaStorage.getMpa(mpaId) == null) {
            throw new IdValidationException("Mpa с ID " + mpaId + " не существует.");
        }
        log.info("Валидация ID mpa пройдена");
    }
}
