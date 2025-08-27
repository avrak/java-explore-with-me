package ru.yandex.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = """
            SELECT *
              FROM events e
             WHERE e.state = 'PUBLISHED'
               AND (CAST(:text AS TEXT) IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', CAST(:text AS TEXT), '%'))
                                   OR LOWER(e.description) LIKE LOWER(CONCAT('%', CAST(:text AS TEXT), '%')) )
               AND (CAST(:categories AS TEXT) IS NULL OR e.category_id IN (CAST(CAST(:categories AS TEXT) AS BIGINT)))
               AND (CAST(:paid AS TEXT) IS NULL OR e.paid = CAST(CAST(:paid AS TEXT) AS BOOLEAN))
               AND (CAST(:rangeStart AS timestamp) IS NULL OR e.event_date >= CAST(:rangeStart AS timestamp))
               AND (CAST(:rangeEnd AS timestamp) IS NULL OR e.event_date < CAST(:rangeEnd AS timestamp))
    """, nativeQuery = true)
    List<Event> findPublishedEvents(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    List<Event> findByInitiatorId(Long userId,
            Pageable pageable);

    @Query(value = """
            SELECT *
              FROM events e
             WHERE (CAST(:users AS TEXT) IS NULL OR e.initiator_id IN (:users))
               AND (CAST(:states AS TEXT) IS NULL OR e.state IN (:states))
               AND (CAST(:categories AS TEXT) IS NULL OR e.category_id IN (:categories))
               AND (CAST(:rangeStart AS TEXT) IS NULL OR e.event_date >= :rangeStart)
               AND (CAST(:rangeEnd AS TEXT) IS NULL OR e.event_date <= :rangeEnd)
    """, nativeQuery = true)
    List<Event> findEventsByAdmin(
            @Param("users") List<Long> users,
            @Param("states") List<EventState> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    Optional<Event> findFirstByCategoryId(Long id);

    Optional<Event> findByIdAndState(Long eventId, EventState eventState);
}