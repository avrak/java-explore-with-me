package ru.yandex.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.EventState;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.exception.model.ForbiddenException;
import ru.yandex.practicum.exception.model.NotFoundException;
import ru.yandex.practicum.request.dto.RequestDto;
import ru.yandex.practicum.request.dto.RequestMapper;
import ru.yandex.practicum.request.model.Request;
import ru.yandex.practicum.request.model.RequestService;
import ru.yandex.practicum.request.model.RequestStatus;
import ru.yandex.practicum.request.repository.RequestRepository;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public Collection<RequestDto> getRequestsByUserId(Long userId) {
        userRepository.findUserById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));

        Collection<Request> requests = requestRepository.findByRequesterId(userId);

        log.info("RequestServiceImpl.getRequestsByUserId: Прочитаны запросы пользователя {}", userId);

        return requests.stream().map(RequestMapper::toDto).toList();
    }

    @Override
    public RequestDto addRequest(Long userId, Long eventId) {
        User requestor = userRepository.findUserById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + userId + " was not found"));

        Optional<Request> requestOpt = requestRepository.findByRequesterIdAndEventId(userId, eventId);

        if(requestOpt.isPresent()) {
            throw new ForbiddenException("Пользователь id=" + userId
                    + " уже подавал заявку на событие id = " + eventId);
        }
        if (event.getEventState() != EventState.PUBLISHED) {
            throw new ForbiddenException("Заявки на неопубликованное событие id=" + eventId + " запрещены");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Автору события запрещено подавать на него заявку");
        }
        if (event.getParticipantLimit() != 0
                && event.getRequests()
                    .stream()
                    .filter(request ->
                            request.getStatus().equals(RequestStatus.CONFIRMED)).count()
                            >= event.getParticipantLimit()) {
            throw new ForbiddenException("Лимит заявок по событию id=" + eventId + " исчерпан");
        }

        Request request = new Request(
                null,
                LocalDateTime.now(),
                event,
                requestor,
                !event.getRequestModeration() || event.getParticipantLimit() == 0
                        ? RequestStatus.CONFIRMED : RequestStatus.PENDING
        );

        log.info("RequestServiceImpl.addRequest: Сохранена заявка userId={}, eventId={}", userId, eventId);

        return RequestMapper.toDto(request);
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {
        User requestor = userRepository.findUserById(userId).orElseThrow(
                () -> new NotFoundException("User with id=" + userId + " was not found"));

        Request request = requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Request with id=" + requestId + " was not found"));

        if (!request.getRequester().getId().equals(userId)) {
            throw new ForbiddenException("Нельзя отменить чужую заявку");
        }

        request.setStatus(RequestStatus.CANCELED);
        request = requestRepository.save(request);
        log.info("RequestServiceImpl.cancelRequest: Заявка с userId={} requestId={} отменена", userId,  requestId);
        return RequestMapper.toDto(request);
    }
}
