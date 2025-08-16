package ru.yandex.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.compilation.dto.CompilationDto;
import ru.yandex.practicum.compilation.dto.CompilationMapper;
import ru.yandex.practicum.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.compilation.dto.UpdateCompilationDto;
import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.compilation.model.CompilationService;
import ru.yandex.practicum.compilation.repository.CompilationRepository;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.exception.model.NotFoundException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Long from, Long size) {
        List<CompilationDto> compilationDtoList = compilationRepository.findByPinnedAndIdBetween(
                        pinned != null && pinned, from, from + size
        ).stream()
                .map(CompilationMapper::toDto)
                .toList();

        log.info("CompilationServiceImpl.getCompilations: Прочитаны подборки pinned={}, from={}, size={}"
                ,pinned, from, size);

        return compilationDtoList.isEmpty() ? Collections.emptyList() : compilationDtoList;
    }

    @Override
    public CompilationDto getCompilationById(Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Compilation with id=" + id +" was not found"));

        log.info("CompilationServiceImpl.getCompilationById: Прочитана подборка id={}", id);

        return CompilationMapper.toDto(compilation);
    }

    @Override
    public CompilationDto addCompilation(NewCompilationDto newDto) {
        Compilation compilation = CompilationMapper.toCompilation(newDto);

        if (newDto.getEventIdList() != null) {
            compilation.setEvents(eventRepository.findAllById(newDto.getEventIdList()));
        }

        compilation = compilationRepository.save(compilation);
        log.info("CompilationServiceImpl.addCompilation: Сохранена подборка {}", compilation);

        return CompilationMapper.toDto(compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);

        log.info("CompilationServiceImpl.deleteCompilation: Удалена подборка {}", compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationDto compDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + compId +" was not found"));

        if (compDto.getPinned() != null) compilation.setPinned(compDto.getPinned());
        if (compilation.getTitle() != null) compilation.setTitle(compilation.getTitle());

        if (compDto.getEventIdList() != null) {
            compilation.setEvents(eventRepository.findAllById(compDto.getEventIdList()));
        }

        compilation = compilationRepository.save(compilation);
        log.info("CompilationServiceImpl.updateCompilation: Изменена подборка {}", compilation);

        return CompilationMapper.toDto(compilation);
    }
}
