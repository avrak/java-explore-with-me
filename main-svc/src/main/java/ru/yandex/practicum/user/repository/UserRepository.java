package ru.yandex.practicum.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(Long userId);

    Collection<User> findUserByIdBetweenFromAndTo(Long from, Long to);

    boolean existsByEmail(String email);

    void deleteUserById(Long userId);
}
