package ru.yandex.practicum.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository  extends JpaRepository<Compilation, Long> {
    List<Compilation> findByPinnedAndIdBetween(Boolean pinned, Long from, Long to);
}
