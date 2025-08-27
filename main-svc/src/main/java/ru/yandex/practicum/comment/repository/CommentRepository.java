package ru.yandex.practicum.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.comment.model.Comment;
import ru.yandex.practicum.event.model.Event;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEvent(Event event, Pageable pageable);
}
