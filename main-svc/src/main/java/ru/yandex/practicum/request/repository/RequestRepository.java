package ru.yandex.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.request.model.Request;
import ru.yandex.practicum.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Collection<Request> findByRequesterId(Long userid);

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);
}
