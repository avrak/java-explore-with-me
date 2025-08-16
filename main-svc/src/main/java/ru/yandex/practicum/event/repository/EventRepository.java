package ru.yandex.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = """
        SELECT e FROM event e
        WHERE e.state = 'PUBLISHED'
        AND (:text IS NULL OR
             LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%')) OR
             LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%')))
        AND (:categories IS NULL OR e.category.id IN :categories)
        AND (:paid IS NULL OR e.paid = :paid)
        AND (:rangeStart IS NULL OR e.eventDate >= :rangeStart)
        AND (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)
        AND (:onlyAvailable = FALSE OR 
             e.participantLimit = 0 OR 
             e.participantLimit > (
                 SELECT COUNT(r) 
                 FROM Request r 
                 WHERE r.event = e 
                 AND r.status = ru.yandex.practicum.request.model.RequestStatus.CONFIRMED
             ))
    """, nativeQuery = true)
    List<Event> findPublishedEvents(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("onlyAvailable") Boolean onlyAvailable,
            Pageable pageable);

    @Query(value = "SELECT e FROM event e WHERE e.initiator.id = :userId", nativeQuery = true)
    List<Event> findByInitiatorId(
            @Param("userId") Long userId,
            Pageable pageable);

    @Query(value = """
            SELECT e
              FROM event e
             WHERE (:users IS NULL OR e.initiator_id IN :users)
               AND (:states IS NULL OR e.state IN :states)
               AND (:categories IS NULL OR e.category_id IN :categories)
               AND (:rangeStart IS NULL OR e.event_date >= :rangeStart)
               AND (:rangeEnd IS NULL OR e.event_date <= :rangeEnd)
    """, nativeQuery = true)
    List<Event> findEventsByAdmin(
            @Param("users") List<Long> users,
            @Param("states") List<EventState> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);
}