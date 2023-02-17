package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Film {
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @PositiveOrZero
    private int duration;
    private Integer rate;
    private Mpa mpa;
    private Set<Genre> genres;
    @JsonIgnore
    private Set<Long> usersId;
}
