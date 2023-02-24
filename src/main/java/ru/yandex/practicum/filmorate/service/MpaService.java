package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {

    @Autowired
    @Qualifier("mpaDBStorage")
    private MpaStorage mpaStorage;

    public List<Mpa> allMpa() {
        return mpaStorage.allMpa();
    }

    public Mpa getMpa(int mpaId) {
        return mpaStorage.getMpa(mpaId);
    }

}
