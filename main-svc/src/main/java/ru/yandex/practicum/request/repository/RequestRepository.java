package ru.yandex.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.request.model.Request;

import java.util.Collection;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Collection<Request> findByRequesterId(Long userid);

    Optional<Request> findByRequesterIdAndEventId(Long userId, Long eventId);

    Collection<Request> findByEventId(Long eventId);

    @Modifying(flushAutomatically = true)
    @Query(value = """
            update requests
               set status = 'REJECTED'
             where event_id = :eventId
               and status = 'PENDING'
            """, nativeQuery = true)
    void rejectHappilessRequests(@Param("eventId") Long eventId);
}
