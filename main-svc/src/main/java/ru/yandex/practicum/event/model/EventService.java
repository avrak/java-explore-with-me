package ru.yandex.practicum.event.model;

import ru.yandex.practicum.event.dto.*;

import java.util.Collection;
import java.util.List;

public interface EventService {
    Collection<EventShortDto> getEventsByAdmin(List<Long> users,
                                               List<EventState> eventStates,
                                               List<Long> categories,
                                               String rangeStart,
                                               String rangeEnd,
                                               Long from,
                                               Long size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequestDto updateDto);

    List<EventShortDto> getEvents(Long userId, Long from, Long size);

    EventFullDto addNewEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventById(Long userId, Long eventId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequestDto updateDto);

    Collection<EventShortDto> getEventList(
            String text,
            List<Long> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Long from,
            Long size
    );

    EventFullDto getFullEventById(Long eventId);
}
