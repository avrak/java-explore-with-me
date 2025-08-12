package ru.yandex.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.request.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request>
}
