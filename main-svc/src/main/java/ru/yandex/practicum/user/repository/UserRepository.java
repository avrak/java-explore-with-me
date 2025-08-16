package ru.yandex.practicum.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(Long userId);

    boolean existsByEmail(String email);

    List<User> findByIdIn(List<Long> idList, Pageable pageable);
}
