package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping("/directors")
    public List<Director> findAll() {
        return directorService.getAll();
    }

    @GetMapping("/directors/{id}")
    public Director findById(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        return directorService.getById(id);
    }

    @PostMapping(value = "/directors")
    public Director create(@RequestBody Director director) throws ValidationException {
        return directorService.add(director);
    }

    @PutMapping(value = "/directors")
    public Director update(@RequestBody Director director) throws ValidationException, NotFoundException {
        return directorService.update(director);
    }

    @DeleteMapping(value = "/directors/{id}")
    public void delete(@PathVariable Long id) {
        directorService.delete(id);
    }

}
