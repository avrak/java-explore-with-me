package ru.yandex.practicum.event.model;

import ru.yandex.practicum.event.dto.EventFullDto;
import ru.yandex.practicum.event.dto.EventShortDto;
import ru.yandex.practicum.event.dto.EventUpdateAdminDto;

import java.util.Collection;
import java.util.List;

public interface EventService {
    Collection<EventShortDto> getEventsByUser(Long userId, Long from, Long size);

    Collection<EventShortDto> getEventsByAdmin(List<Long> users,
                                         List<State> states,
                                         List<Long> categories,
                                         String rangeStart,
                                         String rangeEnd,
                                         Long from,
                                         Long size);

    EventFullDto updateEventByAdmin(Long eventId, EventUpdateAdminDto updateDto);
}
