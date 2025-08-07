package ru.yandex.practicum.event.model;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import ru.yandex.practicum.event.dto.EventShortDto;

import java.util.List;

public interface EventService {
    List<EventShortDto> getEventsByUser(Long userId, Long from, Long size);

    List<EventShortDto> getEventsByAdmin(List<Long> users,
                                         List<State> states,
                                         List<Long> categories,
                                         String rangeStart,
                                         String rangeEnd,
                                         Long from,
                                         Long size);
}
