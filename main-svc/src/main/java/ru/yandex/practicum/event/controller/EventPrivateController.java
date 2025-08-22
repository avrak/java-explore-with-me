package ru.yandex.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.dto.EventFullDto;
import ru.yandex.practicum.event.dto.EventShortDto;
import ru.yandex.practicum.event.dto.NewEventDto;
import ru.yandex.practicum.event.dto.UpdateEventUserRequestDto;
import ru.yandex.practicum.event.service.EventServiceImpl;
import ru.yandex.practicum.request.dto.EventRequestStatusUpdateRequestDto;
import ru.yandex.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.yandex.practicum.request.dto.RequestDto;
import ru.yandex.practicum.request.service.RequestServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Validated
public class EventPrivateController {
    @Autowired
    private final EventServiceImpl eventService;
    @Autowired
    private final RequestServiceImpl requestService;

    @GetMapping
    public List<EventShortDto> getUserEvents(
            @PathVariable @Positive Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.info("GET/users/{}/events: Получить события пользователя from={}, size={}", userId, from, size);
        return eventService.getEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(
            @PathVariable @Positive Long userId,
            @RequestBody @Valid NewEventDto newEventDto
    ) {
        log.info("POST/users/{}/events: Сохранить событие newEventDto={}", userId, newEventDto.toString());
        return eventService.addNewEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        log.info("GET/users/{}/events/{}: Получить событие пользователя", userId, eventId);
        return eventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid UpdateEventUserRequestDto updateEventUserRequestDto) {
        log.info("PATCH/users/{}/events/{}: Изменить событие пользователя", userId, eventId);
        return eventService.updateEventByUser(userId, eventId, updateEventUserRequestDto);
    }

    @GetMapping("{eventId}/requests")
    public List<RequestDto> getRequestsByUserAndEvent(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        log.info("GET/users/{}/events/{}/requests: Получить запросы события пользователя", userId, eventId);
        return requestService.getRequestsByUserAndEvent(userId, eventId);
    }

    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResultDto updateRequestStatus(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid EventRequestStatusUpdateRequestDto requestDto
    ) {
        log.info("PATCH/users/{}/events/{}/requests: Обновить статусы запросов пользователя для события", userId, eventId);
        return requestService.updateRequestStatus(userId, eventId, requestDto);
    }
}
