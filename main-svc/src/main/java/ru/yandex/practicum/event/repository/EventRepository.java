package ru.yandex.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.event.dto.EventShortDto;
import ru.yandex.practicum.event.model.Event;

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
             where e.initiatorId = :userId
               and e.id between :from and :from + :size
            """)
    List<EventShortDto> findEventsByUser(
            @Param("userId") Long userId,
            @Param("from") Long from,
            @Param("size") Long size);

}
