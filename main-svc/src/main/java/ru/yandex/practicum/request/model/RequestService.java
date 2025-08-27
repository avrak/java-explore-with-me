package ru.yandex.practicum.request.model;

import ru.yandex.practicum.request.dto.EventRequestStatusUpdateRequestDto;
import ru.yandex.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.yandex.practicum.request.dto.RequestDto;

import java.util.Collection;
import java.util.List;

public interface RequestService {
    Collection<RequestDto> getRequestsByUserId(Long userId);

    RequestDto addRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);

    List<RequestDto> getRequestsByUserAndEvent(Long userId, Long eventId);

    EventRequestStatusUpdateResultDto updateRequestStatus(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequestDto requestDto
    );
}
