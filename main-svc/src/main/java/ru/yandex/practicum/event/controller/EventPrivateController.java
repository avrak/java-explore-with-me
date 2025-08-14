package ru.yandex.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.dto.EventFullDto;
import ru.yandex.practicum.event.dto.EventShortDto;
import ru.yandex.practicum.event.dto.NewEventDto;
import ru.yandex.practicum.event.dto.UpdateEventUserRequestDto;
import ru.yandex.practicum.event.model.EventService;
import ru.yandex.practicum.request.dto.RequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(
            @PathVariable @Positive Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Long from,
            @RequestParam(defaultValue = "10") @Positive Long size
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
}
