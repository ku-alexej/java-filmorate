package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping("/directors")
    public List<Director> findAll() {
        log.info("Get /directors : запрос списка фильмов.");
        return directorService.getAll();
    }

    @GetMapping("/directors/{id}")
    public Director findById(@PathVariable Long id) {
        log.debug("Get /directors/" + id + " : запрос режиссёра.");
        return directorService.getById(id);
    }

    @PostMapping(value = "/directors")
    public Director create(@RequestBody Director director) {
        log.debug("Post /directors : добавление режиссёра {}.", director);
        return directorService.add(director);
    }

    @PutMapping(value = "/directors")
    public Director update(@RequestBody Director director) {
        log.debug("Put /directors : обновление данных режиссёра {}.", director);
        return directorService.update(director);
    }

    @DeleteMapping(value = "/directors/{id}")
    public void delete(@PathVariable Long id) {
        log.debug("Delete /directors/" + id + " : удаление данных режиссёра.");
        directorService.delete(id);
    }

}
