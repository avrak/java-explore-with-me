package ru.yandex.practicum.comment.dto;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.comment.model.Comment;
import ru.yandex.practicum.comment.model.CommentState;
import ru.yandex.practicum.event.dto.EventMapper;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.user.dto.UserMapper;
import ru.yandex.practicum.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public Comment toComment(NewCommentDto newCommentDto, User author, Event event) {
        return new Comment(
                null,
                event,
                author,
                newCommentDto.getText(),
                CommentState.PENDING,
                LocalDateTime.now(),
                null,
                null
        );
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                EventMapper.toShortDto(comment.getEvent()),
                UserMapper.toUserShortDto(comment.getAuthor()),
                comment.getText(),
                comment.getState(),
                comment.getCreatedOn(),
                comment.getUpdatedOn() != null ? comment.getUpdatedOn() : null,
                comment.getPublishedOn() != null ? comment.getPublishedOn() : null
        );
    }
}
