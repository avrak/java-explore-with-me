package ru.yandex.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.category.repository.CategoryRepository;
import ru.yandex.practicum.event.dto.*;
import ru.yandex.practicum.event.model.*;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.exception.model.ForbiddenException;
import ru.yandex.practicum.exception.model.NotFoundException;
import ru.yandex.practicum.location.dto.LocationMapper;
import ru.yandex.practicum.location.model.Location;
import ru.yandex.practicum.location.repository.LocationRepository;

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
            List<State> states,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            Long from,
            Long size
    ) {
        List<EventShortDto> shortDtoList = eventRepository.findEvents(
                users,
                states,
                categories,
                LocalDateTime.parse(rangeStart, formatter),
                LocalDateTime.parse(rangeEnd, formatter),
                from,
                size
        );

        log.info("EventService.getEventsByAdmin: " +
                "Прочитаны данные для users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        return shortDtoList;
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, EventUpdateAdminDto updateDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие id=" + eventId + " не найдено"));

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
                if (event.getState() != State.PENDING) {
                    throw new ForbiddenException("Запрещено публиковать событие в статусе " + event.getState());
                }
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            case StateActionAdmin.REJECT_EVENT:
                if (event.getState() == State.PUBLISHED) {
                    throw new ForbiddenException("Запрещено отклонять событие в статусе " + event.getState());
                }
                event.setState(State.CANCELED);
        }

        event = eventRepository.save(event);

        log.info("EventService.updateEventByAdmin: Сохранены изменения события id={}, {}",
                eventId, updateDto.toString());

        return EventMapper.toFullDto(event);
    }
}
