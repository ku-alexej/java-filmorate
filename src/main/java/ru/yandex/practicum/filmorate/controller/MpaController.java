package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {

    @Autowired
    private MpaService mpaService;

    @GetMapping()
    public List<Mpa> allMpa() {
        log.info("Get /mpa : запрос списка возрастных рейтингов");
        return mpaService.allMpa();
    }

    @GetMapping("/{mpaId}")
    public Mpa getMpa(@PathVariable int mpaId) {
        log.debug("Get /mpa/" + mpaId + " : запрос возрастного рейтинга");
        return mpaService.getMpa(mpaId);
    }
}
