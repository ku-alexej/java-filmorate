package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;
import java.util.Optional;

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
        return Optional.ofNullable(mpaStorage.getMpa(mpaId))
                .orElseThrow(() -> new EntityNotFoundException("Mpa с ID " + mpaId + " не существует"));
    }

}
