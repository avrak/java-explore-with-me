package ru.yandex.practicum.event.dto;

import ru.yandex.practicum.category.dto.CategoryMapper;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.user.dto.UserMapper;

public class EventMapper {
    public static EventShortDto toShortDto(Event event, int confirmedRequests, int views) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                confirmedRequests,
                event.getEventDate(),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }
}
