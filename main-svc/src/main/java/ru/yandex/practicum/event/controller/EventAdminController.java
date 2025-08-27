package ru.yandex.practicum.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.event.dto.EventFullDto;
import ru.yandex.practicum.event.dto.UpdateEventAdminRequestDto;
import ru.yandex.practicum.event.model.EventService;
import ru.yandex.practicum.event.model.EventState;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public Collection<EventFullDto> getEventList(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> eventStates,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("GET/admin/events: users={}, eventStates={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, eventStates, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsByAdmin(users, eventStates, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable @Positive Long eventId,
                                    @RequestBody @Valid UpdateEventAdminRequestDto updateDto) {
        log.info("PATCH/admin/events{}: {}", eventId, updateDto);
        return eventService.updateEventByAdmin(eventId, updateDto);
    }

}
