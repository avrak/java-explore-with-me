package ru.yandex.practicum.compilation.model;

import ru.yandex.practicum.compilation.dto.CompilationDto;
import ru.yandex.practicum.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.compilation.dto.UpdateCompilationDto;

import java.util.Collection;
import java.util.List;

public interface CompilationService {
    Collection<CompilationDto> getCompilations(Boolean pinned, Long from, Long size);

    CompilationDto getCompilationById(Long id);

    CompilationDto addCompilation(NewCompilationDto newDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationDto compDto);
}
