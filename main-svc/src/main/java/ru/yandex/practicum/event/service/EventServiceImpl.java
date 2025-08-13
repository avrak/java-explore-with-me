package ru.yandex.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.category.repository.CategoryRepository;
import ru.yandex.practicum.event.dto.*;
import ru.yandex.practicum.event.model.*;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.exception.model.ConflictException;
import ru.yandex.practicum.exception.model.ForbiddenException;
import ru.yandex.practicum.exception.model.NotFoundException;
import ru.yandex.practicum.location.dto.LocationMapper;
import ru.yandex.practicum.location.model.Location;
import ru.yandex.practicum.location.repository.LocationRepository;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, Long from, Long size) {
        List<EventShortDto> list = eventRepository.findEventsByUser(userId, from, size);
        log.info("EventService.getEventsByUser: Прочитаны данные для userId={}, from={}, to={}", userId, from, size);
        return list;
    }

    @Override
    public List<EventShortDto> getEventsByAdmin(
            List<Long> users,
            List<EventState> eventStates,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            Long from,
            Long size
    ) {
        List<EventShortDto> shortDtoList = eventRepository.findEvents(
                users,
                eventStates,
                categories,
                LocalDateTime.parse(rangeStart, formatter),
                LocalDateTime.parse(rangeEnd, formatter),
                from,
                size
        );

        log.info("EventService.getEventsByAdmin: " +
                "Прочитаны данные для users={}, eventStates={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, eventStates, categories, rangeStart, rangeEnd, from, size);

        return shortDtoList;
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequestDto updateDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (updateDto.getTitle() != null) {
            event.setTitle(updateDto.getTitle());
        }
        if (updateDto.getAnnotation() != null) {
            event.setAnnotation(updateDto.getAnnotation());
        }
        if (updateDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateDto.getCategory()).orElse(null));
        }
        if (updateDto.getDescription() != null) {
            event.setDescription(updateDto.getDescription());
        }
        if (updateDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(updateDto.getEventDate(), formatter));
        }
        if (updateDto.getLocation() != null) {
            Location location = LocationMapper.toLocation(updateDto.getLocation());
            event.setLocation(locationRepository.save(location));
        }
        if (updateDto.getPaid() != null) {
            event.setPaid(updateDto.getPaid());
        }
        if (updateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateDto.getParticipantLimit());
        }
        if (updateDto.getRequestModeration() != null) {
            event.setRequestModeration(updateDto.getRequestModeration());
        }

        switch (updateDto.getStateAction()) {
            case StateActionAdmin.PUBLISH_EVENT:
                if (event.getEventState() != EventState.PENDING) {
                    throw new ForbiddenException("Запрещено публиковать событие в статусе " + event.getEventState());
                }
                event.setEventState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            case StateActionAdmin.REJECT_EVENT:
                if (event.getEventState() == EventState.PUBLISHED) {
                    throw new ForbiddenException("Запрещено отклонять событие в статусе " + event.getEventState());
                }
                event.setEventState(EventState.CANCELED);
        }

        event = eventRepository.save(event);

        log.info("EventService.updateEventByAdmin: Сохранены изменения события id={}, {}",
                eventId, updateDto.toString());

        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Long from, Long size) {
        User user = userRepository.findUserById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));

        List<EventShortDto> eventShortDtoList = eventRepository.findEventsByUser(userId, from, size);

        log.info("EventService.getEvents: Прочитаны события userId={}, from={}, size={}", userId, from, size);

        return eventShortDtoList;
    }

    @Override
    public EventFullDto addNewEvent(Long userId, NewEventDto newEventDto) {
        User initiator = userRepository.findUserById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));

        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(
                () -> new NotFoundException("Category with id=" + userId + " was not found"));

        Location location = locationRepository.save(LocationMapper.toLocation(newEventDto.getLocation()));

        Event event = EventMapper.toEvent(newEventDto, initiator, category, location);

        if (event.getCreatedOn().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Новое событие можно добавить не позднее чем за два часа до начала");
        }

        event = eventRepository.save(event);

        log.info("EventService.addNewEvent: Сохранено событие userId={}, newEventDto={}", userId, newEventDto);

        return EventMapper.toFullDto(event);
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        userRepository.findUserById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        log.info("EventService.getEventById: Прочитано событие userId={}, eventId={}", userId, eventId);
        return EventMapper.toFullDto(event);
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequestDto updateDto){
        User user = userRepository.findUserById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Событие eventId=" + eventId
                    + " не принадлежит userId=" + userId + ". Изменение запрещено");
        }

        if (event.getEventState() == EventState.PUBLISHED) {
            throw new ForbiddenException("Запрещено менять событие в статусе " + event.getEventState());
        }


    }
}
