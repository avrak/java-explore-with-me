package ru.yandex.practicum.event.dto;

import ru.yandex.practicum.category.dto.CategoryMapper;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.EventState;
import ru.yandex.practicum.location.dto.LocationMapper;
import ru.yandex.practicum.location.model.Location;
import ru.yandex.practicum.request.model.Request;
import ru.yandex.practicum.request.model.RequestStatus;
import ru.yandex.practicum.user.dto.UserMapper;
import ru.yandex.practicum.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static long countRequests(List<Request> requestList) {
        return requestList == null ? 0 :
                requestList.stream().filter(request -> request.getStatus() == RequestStatus.CONFIRMED).count();
    }

    public static EventShortDto toShortDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                countRequests(event.getRequests()),
                event.getEventDate(),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static EventFullDto toFullDto(Event event) {
        return new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                countRequests(event.getRequests()),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate().format(formatter),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                LocationMapper.toDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn() != null ? event.getPublishedOn().format(formatter) : null,
                event.getRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static Event toEvent(NewEventDto newEventDto, User initiator, Category category, Location location) {
        return new Event(
                null,
                newEventDto.getAnnotation(),
                category,
                LocalDateTime.now(),
                newEventDto.getDescription(),
                LocalDateTime.parse(newEventDto.getEventDate(), formatter),
                initiator,
                location,
                newEventDto.isPaid(),
                newEventDto.getParticipantLimit(),
                null,
                newEventDto.isRequestModeration(),
                EventState.PENDING,
                newEventDto.getTitle(),
                null,
                0L
        );
    }
}
