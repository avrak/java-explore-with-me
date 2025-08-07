package ru.yandex.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.event.spi.EventManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.event.dto.EventShortDto;
import ru.yandex.practicum.event.dto.EventMapper;
import ru.yandex.practicum.event.model.EventService;
import ru.yandex.practicum.event.model.State;
import ru.yandex.practicum.event.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** TODO
     * Дообогатить list значениями views из сервиса статистики
     */
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
        LocalDateTime startedAt = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime endedAt = LocalDateTime.parse(rangeEnd, formatter);

        List<EventShortDto> list = eventRepository.findAll()
                .stream()
                .filter(event -> users.contains(event.getInitiatorId()) || users.isEmpty())
                .filter(event -> states.contains(event.getState()) || states.isEmpty())
                .filter(event -> categories.contains(event.getCategory().getId()) || categories.isEmpty())
                .filter(event -> event.getEventDate().isAfter(startedAt))
                .filter(event -> event.getEventDate().isBefore(endedAt))
                .filter(event -> event.getId() >= from && event.getId() <= from + size)
                .map(event -> EventMapper.toShortDto(event, ))
                .toList();

        log.info("EventService.getEventsByAdmin: " +
                "Прочитаны данные для users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);

        return list;
    }
}
