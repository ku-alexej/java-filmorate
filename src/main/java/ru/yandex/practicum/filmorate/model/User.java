package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    private long id;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String login;
    private String name;
    @NotNull
    private LocalDate birthday;
    @JsonIgnore
    private final Set<Long> friendsId = new HashSet<Long>();
}
