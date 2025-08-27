package ru.yandex.practicum.comment.model;

import jakarta.transaction.Transactional;
import ru.yandex.practicum.comment.dto.CommentDto;
import ru.yandex.practicum.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    @Transactional
    void deleteComment(Long userId, Long commentId);

    @Transactional
    CommentDto updateCommentStatusByAdmin(Long commentId, boolean isConfirm);

    List<CommentDto> getEventComments(Long eventId, int from, int size);

    CommentDto getCommentById(Long commentId);
}
