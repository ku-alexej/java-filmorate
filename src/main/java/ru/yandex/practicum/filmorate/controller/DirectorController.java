package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<Director> findAll() {
        log.info("GET /directors : get list of all directors");
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public Director findById(@PathVariable Long id) {
        log.debug("GET /directors/{} : get director by ID", id);
        return directorService.getById(id);
    }

    @PostMapping
    public Director create(@RequestBody Director director) {
        log.debug("POST /directors : create director - {}", director);
        return directorService.add(director);
    }

    @PutMapping
    public Director update(@RequestBody Director director) {
        log.debug("PUT /directors : update director - {}", director);
        return directorService.update(director);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.debug("DELETE /directors/{} : delete director by ID.", id);
        directorService.delete(id);
    }

}
