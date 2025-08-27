package ru.yandex.practicum.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.comment.dto.CommentDto;
import ru.yandex.practicum.comment.dto.CommentMapper;
import ru.yandex.practicum.comment.dto.NewCommentDto;
import ru.yandex.practicum.comment.model.Comment;
import ru.yandex.practicum.comment.model.CommentService;
import ru.yandex.practicum.comment.model.CommentState;
import ru.yandex.practicum.comment.repository.CommentRepository;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.EventState;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.exception.model.ConflictException;
import ru.yandex.practicum.exception.model.NotFoundException;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = getUserOrException(userId);
        Event event = getEventOrException(eventId);

        CommentDto commentDto = CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(newCommentDto, user, event)));

        log.info("CommentServiceImpl.createComment: Сохранен комментарий userId={}, eventId={}, newCommentDto={}",
                userId, eventId, newCommentDto);

        return commentDto;
    }

    @Transactional
    @Override
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto newCommentDto) {
        getUserOrException(userId);
        Comment comment = getCommentOrException(commentId);

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Изменение чужого комментария запрещено");
        }

        if (comment.getState().equals(CommentState.CONFIRMED)) {
            throw new ConflictException("Изменение подтверждённого комментария запрещено");
        }

        comment.setText(newCommentDto.getText());
        comment.setUpdatedOn(LocalDateTime.now());
        comment.setState(CommentState.PENDING);

        CommentDto commentDto = CommentMapper.toCommentDto(commentRepository.save(comment));

        log.info("CommentServiceImpl.updateComment: Обновлён комментарий userId={}, commentId={}, newCommentDto={}",
                userId, commentId, newCommentDto);

        return commentDto;
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long commentId) {
        getUserOrException(userId);
        Comment comment = getCommentOrException(commentId);

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Удаление чужого комментария запрещено");
        }

        if (comment.getState().equals(CommentState.CONFIRMED)) {
            throw new ConflictException("Удаление подтверждённого комментария запрещено");
        }

        commentRepository.deleteById(commentId);

        log.info("CommentServiceImpl.deleteComment: Удалён комментарий userId={}, commentId={}",
                userId, commentId);
    }

    @Transactional
    @Override
    public CommentDto updateCommentStatusByAdmin(Long commentId, boolean isConfirm) {
        Comment comment = getCommentOrException(commentId);

        comment.setState(isConfirm ? CommentState.CONFIRMED : CommentState.REJECTED);
        comment.setPublishedOn(LocalDateTime.now());

        CommentDto commentDto = CommentMapper.toCommentDto(commentRepository.save(comment));
        log.info("CommentServiceImpl.updateCommentStatusByAdmin: Обновлён статус комментария commentId={}, isConfirm={}",
                commentId, isConfirm);

        return commentDto;
    }

    @Override
    public List<CommentDto> getEventComments(Long eventId, int from, int size) {
        Event event = getEventOrException(eventId);

        List<CommentDto> commentDtoList = commentRepository
                .findByEvent(event, PageRequest.of(from / size, size))
                .stream()
                .map(CommentMapper::toCommentDto)
                .toList();

        log.info("CommentServiceImpl.getEventComments: Получены комментарии eventId={}, from={}, size={}",
                eventId, from, size);

        return commentDtoList;
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        CommentDto commentDto = CommentMapper.toCommentDto(getCommentOrException(commentId));
        log.info("CommentServiceImpl.getCommentById: Получен комментарий commentId={}", commentId);
        return commentDto;
    }

    private User getUserOrException(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    private Event getEventOrException(Long eventId) {
        return eventRepository.findByIdAndState(eventId, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Опубликованное событие с id=" + eventId + " не найдено"));
    }

    private Comment getCommentOrException(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий с id=" + commentId + " не найден"));
    }
}
