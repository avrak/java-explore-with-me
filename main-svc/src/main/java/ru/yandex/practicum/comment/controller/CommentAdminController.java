package ru.yandex.practicum.comment.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.comment.dto.CommentDto;
import ru.yandex.practicum.comment.model.CommentService;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping("/admin/comment")
public class CommentAdminController {
    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public CommentDto updateCommentStatusByAdmin(@PathVariable @Positive Long commentId,
                                                 @RequestParam boolean isConfirm) {
        log.info("PATCH/admin/comment/{}: Обновить статус комментария {}", commentId, isConfirm);
        return commentService.updateCommentStatusByAdmin(commentId, isConfirm);
    }
}
