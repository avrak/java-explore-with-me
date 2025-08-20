package ru.yandex.practicum.request.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.request.dto.RequestDto;
import ru.yandex.practicum.request.service.RequestServiceImpl;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class RequestPrivateController {
    @Autowired
    private final RequestServiceImpl requestService;

    @GetMapping
    public Collection<RequestDto> getRequestByUserId(@PathVariable @Positive Long userId) {
        log.info("GET/users/{}/requests: Прочитать запросы пользователя", userId);

        return requestService.getRequestsByUserId(userId);
    }

    @PostMapping
    public RequestDto addRequest(
            @PathVariable @Positive Long userId,
            @RequestParam @Positive Long eventId
    ) {
        log.info("POST/users/{}/requests: Сохранить заявку на событие {}", userId, eventId);

        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long requestId
    ) {
        log.info("PATCH//users/{}/requests/{}/cancel: Отменить заявку", userId, requestId);

        return requestService.cancelRequest(userId, requestId);
    }

}
