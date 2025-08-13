package ru.yandex.practicum.request.model;

import ru.yandex.practicum.request.dto.RequestDto;

import java.util.Collection;

public interface RequestService {
    Collection<RequestDto> getRequestsByUserId(Long userId);

    RequestDto addRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);
}
