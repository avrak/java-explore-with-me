package ru.yandex.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.StatClient;
import ru.yandex.practicum.StatisticsGetDto;
import ru.yandex.practicum.StatisticsPostDto;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.category.repository.CategoryRepository;
import ru.yandex.practicum.event.dto.*;
import ru.yandex.practicum.event.model.*;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.exception.model.ConflictException;
import ru.yandex.practicum.exception.model.NotFoundException;
import ru.yandex.practicum.exception.model.ParameterNotValidException;
import ru.yandex.practicum.location.dto.LocationMapper;
import ru.yandex.practicum.location.model.Location;
import ru.yandex.practicum.location.repository.LocationRepository;
import ru.yandex.practicum.request.model.RequestStatus;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventServiceImpl implements EventService {
    private final String APPNAME = "ewm-main-service";
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    @Autowired
    private final StatClient statClient;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventFullDto> getEventsByAdmin(
            List<Long> users,
            List<EventState> eventStates,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size
    ) {
        List<EventFullDto> dtoList = eventRepository.findEventsByAdmin(
                users,
                eventStates,
                categories,
                rangeStart == null ? null : LocalDateTime.parse(rangeStart, formatter),
                rangeEnd == null ? null : LocalDateTime.parse(rangeEnd, formatter),
                PageRequest.of(from / size, size)
        ).stream()
                .map(EventMapper::toFullDto)
                .toList();

        log.info("EventService.getEventsByAdmin: " +
                "Прочитаны данные для users={}, eventStates={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, eventStates, categories, rangeStart, rangeEnd, from, size);

        return dtoList;
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
            LocalDateTime eventDate = LocalDateTime.parse(updateDto.getEventDate(), formatter);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ParameterNotValidException("Дата события должна быть не ранее, чем через 1 час");
            }
            event.setEventDate(eventDate);
        }
        if (updateDto.getLocation() != null) {
            Location location = LocationMapper.toLocation(updateDto.getLocation());
            event.setLocation(locationRepository.save(location));
        }
        if (updateDto.getPaid() != null) {
            event.setPaid(updateDto.getPaid());
        }
        if (updateDto.getParticipantLimit() != null) {
            if (updateDto.getParticipantLimit() < 0) {
                throw new ParameterNotValidException("Количество участников события должно быть больше или равно нулю");
            }
            event.setParticipantLimit(updateDto.getParticipantLimit());
        }
        if (updateDto.getRequestModeration() != null) {
            event.setRequestModeration(updateDto.getRequestModeration());
        }

        if (updateDto.getStateAction() != null) {
            switch (updateDto.getStateAction()) {
                case StateActionAdmin.PUBLISH_EVENT:
                    if (event.getState() != EventState.PENDING) {
                        throw new ConflictException("Запрещено публиковать событие в статусе " + event.getState());
                    }
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case StateActionAdmin.REJECT_EVENT:
                    if (event.getState() == EventState.PUBLISHED) {
                        throw new ConflictException("Запрещено отклонять событие в статусе " + event.getState());
                    }
                    event.setState(EventState.CANCELED);
            }
        }

        event = eventRepository.save(event);

        log.info("EventService.updateEventByAdmin: Сохранены изменения события id={}, {}",
                eventId, updateDto);

        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        userRepository.findUserById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));

        List<EventShortDto> eventShortDtoList = eventRepository.findByInitiatorId(
                userId, PageRequest.of(from / size, size))
                .stream()
                .map(EventMapper::toShortDto)
                .toList();

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

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ParameterNotValidException("Новое событие можно добавить не позднее чем за два часа до начала");
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
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequestDto updateDto) {
        userRepository.findUserById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Событие eventId=" + eventId
                    + " не принадлежит userId=" + userId + ". Изменение запрещено");
        }

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Запрещено менять событие в статусе " + event.getState());
        }

        if (updateDto.getAnnotation() != null) {
            event.setAnnotation(updateDto.getAnnotation());
        }

        if (updateDto.getCategory() != null) {
            Category category = categoryRepository.findById(updateDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id=" + eventId + " was not found"));
            event.setCategory(category);
        }

        if (updateDto.getDescription() != null) {
            event.setDescription(updateDto.getDescription());
        }

        if (updateDto.getTitle() != null) {
            event.setTitle(updateDto.getTitle());
        }

        if (updateDto.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateDto.getEventDate(), formatter);
            if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ParameterNotValidException("Дата события должна быть не ранее, чем через 1 час");
            }
            event.setEventDate(eventDate);
        }

        if (updateDto.getLocation() != null) {
            Location location = event.getLocation();
            location.setLat(updateDto.getLocation().getLat());
            location.setLon(updateDto.getLocation().getLon());
            event.setLocation(location);
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

        if (updateDto.getStateAction() == StateActionUser.SEND_TO_REVIEW) {
            event.setState(EventState.PENDING);
        } else if (updateDto.getStateAction() == StateActionUser.CANCEL_REVIEW) {
            event.setState(EventState.CANCELED);
        }

        event = eventRepository.save(event);

        log.info("EventService.updateEventByUser: Сохранены изменения для userId={}, eventId={}, updateDto={}",
                userId, eventId, updateDto);

        return EventMapper.toFullDto(event);
    }

    @Override
    public Collection<EventShortDto> getEventList(
            String text,
            List<Long> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size,
            String ip,
            String uri
    ) {
        statClient.hit(new StatisticsPostDto(APPNAME, ip, uri, LocalDateTime.now()));
        EventSort eventSort = null;

        if (sort != null) {
            try {
                eventSort = EventSort.valueOf(sort);
            } catch (IllegalArgumentException e) {
                throw new ParameterNotValidException("Передан некорректный параметр для сортировки sort=" + sort);
            }
        }

        LocalDateTime start;
        LocalDateTime end;

        try {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        } catch (Exception e) {
            throw new ParameterNotValidException("Переданы даты в некорректном формате");
        }

        if (start.isAfter(end)) {
            throw new ParameterNotValidException("Даты переданы некорректно: конец раньше начала");
        }

        Collection<Event> events = eventRepository.findPublishedEvents(text, categories, paid, start, end,
                PageRequest.of(from / size, size));

        List<EventShortDto> eventList = new ArrayList<>();

        if (events.isEmpty()) {
            return eventList;
        }

        if (onlyAvailable) {
            events = events
                    .stream()
                    .filter(e -> e.getRequests()
                            .stream()
                            .filter(r -> r.getStatus() == RequestStatus.CONFIRMED)
                            .count() < e.getParticipantLimit())
                    .toList();
        }

        eventList = events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());

        if (eventSort != null && !eventList.isEmpty()) {
            switch (eventSort) {
                case EVENT_DATE -> eventList.sort(
                        Comparator.comparing(
                                EventShortDto::getEventDate,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        )
                );
                case VIEWS -> eventList.sort(
                        Comparator.comparing(
                                EventShortDto::getViews,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        )
                );
            }
        }

        log.info("EventService.getEventList: Прочитаны запросы пользователя для text={}, categories={}, paid={}, "
                        + "rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        return eventList;
    }

    @Override
    public EventFullDto getFullEventById(Long eventId, String ip, String uri) {
        // Получим уникальное количество просмотров до текущего
        int viewsBefore = getViewsFromStats(uri);

        statClient.hit(new StatisticsPostDto(APPNAME, uri, ip, LocalDateTime.now()));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event with id=" + eventId + " was not published");
        }


        if (viewsBefore < getViewsFromStats(uri)) {
            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
        }

        EventFullDto fullDto = EventMapper.toFullDto(event);
        log.info("EventService.getFullEventById: Прочитано событие eventId={}", eventId);
        return fullDto;
    }

    private int getViewsFromStats(String uri) {

        ResponseEntity<Object> response = statClient.getStat(
                        LocalDateTime.of(1900, Month.JANUARY, 1, 0, 0),
                        LocalDateTime.now(),
                        new ArrayList<>(Collections.singleton(uri)),
                        true);

        if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
            List<StatisticsGetDto> statList = (List<StatisticsGetDto>) response.getBody();
            assert statList != null;
            return statList.size();
        }

        return 0;
    }
}
