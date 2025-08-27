package ru.yandex.practicum.compilation.dto;

import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.event.dto.EventMapper;

import java.util.ArrayList;

public class CompilationMapper {
    public static CompilationDto toDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getEvents() == null ? new ArrayList<>()
                        : compilation.getEvents().stream().map(EventMapper::toShortDto).toList(),
                compilation.getPinned()
        );
    }

    public static Compilation toCompilation(NewCompilationDto compilationDto) {
        return new Compilation(
                null,
                null,
                compilationDto.getPinned(),
                compilationDto.getTitle()
        );
    }

    public static NewCompilationDto toNewDto(Compilation compilation) {
        return new NewCompilationDto(
                null,
                compilation.getPinned(),
                compilation.getTitle()
        );
    }
}
