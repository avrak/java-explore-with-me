package ru.yandex.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.event.dto.EventShortDto;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
            select new ru.yandex.practicum.EventShortDto(
                    e.annotation,
                    new CategoryDto(
                        e.category.id,
                        e.category.name
                    ),
                    (
                        select count(r.id)
                          from Request r
                         where r.event.id = e.id
                           and r.status = 'CONFIRMED'
                    )
                    e.eventDate,
                    e.id,
                    new ru.yandex.practicum.user.dto.UserShortDto(
                        e.initiator.id,
                        e.initiator.name
                    ),
                    e.paid,
                    e.title,
                    e.views
                )
              from Event e
             where e.initiatorId = :userId
               and e.id between :from and :from + :size
            """)
    List<EventShortDto> findEventsByUser(
            @Param("userId") Long userId,
            @Param("from") Long from,
            @Param("size") Long size);

    @Query("""
            select new ru.yandex.practicum.EventShortDto(
                    e.annotation,
                    new CategoryDto(
                        e.category.id,
                        e.category.name
                    ),
                    (
                        select count(r.id)
                          from Request r
                         where r.event.id = e.id
                           and r.status = 'CONFIRMED'
                    )
                    e.eventDate,
                    e.id,
                    new ru.yandex.practicum.user.dto.UserShortDto(
                        e.initiator.id,
                        e.initiator.name
                    ),
                    e.paid,
                    e.title,
                    e.views
                )
              from Event e
             where (e.initiator.id in (:users) or cast(:users as text) is null)
               and (e.state in (:eventStates) or cast(:eventStates as text) is null)
               and (e.category.id in (:categories) or cast(:categories as text) is null)
               and (e.eventDate >= :rangeStart or cast(:rangeStart as timestamp) is null)
               and (e.eventDate <= :rangeEnd or cast(:rangeEnd as timestamp) is null)
               and e.id between :from and :from + :size
            """)
    List<EventShortDto> findEvents(
            @Param("users") List<Long> users,
            @Param("eventStates") List<EventState> eventStates,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("from") Long from,
            @Param("size") Long size);

    @Query("""
            select new ru.yandex.practicum.EventShortDto(
                    e.annotation,
                    new CategoryDto(
                        e.category.id,
                        e.category.name
                    ),
                    (
                        select count(r.id)
                          from Request r
                         where r.event.id = e.id
                           and r.status = 'CONFIRMED'
                    )
                    e.eventDate,
                    e.id,
                    new ru.yandex.practicum.user.dto.UserShortDto(
                        e.initiator.id,
                        e.initiator.name
                    ),
                    e.paid,
                    e.title,
                    e.views
                )
              from Event e
             where e.state = EventState.PUBLISHED
               and (lower(e.annotation) like concat('%', lower(cast(:text as text)), '%')
                    or lower(e.description) like concat('%', lower(cast(:text as text)), '%')
                    or cast(:users as text) is null)
               and (e.state in (:eventStates) or cast(:eventStates as text) is null)
               and (e.category.id in (:categories) or cast(:categories as text) is null)
               and (e.eventDate >= :rangeStart or cast(:rangeStart as timestamp) is null)
               and (e.eventDate <= :rangeEnd or cast(:rangeEnd as timestamp) is null)
               and e.id between :from and :from + :size
            """)
    List<EventShortDto> findPublishedEvents(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") String rangeStart,
            @Param("rangeEnd") String rangeEnd,
            @Param("onlyAvailable") Boolean onlyAvailable,
            @Param("sort") String sort,
            @Param("from") Long from,
            @Param("size") Long size);
}
