package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDBStorage;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

//@SpringBootTest
//class FilmorateApplicationTests {
//
//	@Test
//	void contextLoads() {
//	}
//
//}

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
//	private final UserDBStorage userStorage;

	@Test
	void contextLoads() {
	}
}
