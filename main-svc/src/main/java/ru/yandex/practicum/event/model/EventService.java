package ru.yandex.practicum.event.model;

import ru.yandex.practicum.event.dto.*;
import ru.yandex.practicum.request.dto.RequestDto;

import java.util.Collection;
import java.util.List;

public interface EventService {
    Collection<EventShortDto> getEventsByUser(Long userId, Long from, Long size);

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

    List<RequestDto> getUserRequestsForEvent(Long userId, Long eventId);
}
