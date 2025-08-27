package ru.yandex.practicum.comment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.comment.dto.CommentDto;
import ru.yandex.practicum.comment.dto.NewCommentDto;
import ru.yandex.practicum.comment.model.CommentService;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/user/{userId}")
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping("/events/{eventId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long eventId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("POST/user/{}/events/{}/comment: Сохранить комментарий {}", userId, eventId, newCommentDto);
        return commentService.createComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/comment/{commentId}")
    public CommentDto updateComment(@PathVariable @Positive Long userId,
                                    @PathVariable @Positive Long commentId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("PATCH/user/{}/comment/{}: Обновить комментарий {}", userId, commentId, newCommentDto);
        return commentService.updateComment(userId, commentId, newCommentDto);
    }

    @DeleteMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long userId,
                              @PathVariable @Positive Long commentId) {
        log.info("DELETE/user/{}/comment/{}: Удалить комментарий", userId, commentId);
        commentService.deleteComment(userId, commentId);
    }


}
