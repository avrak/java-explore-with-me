package ru.yandex.practicum.compilation.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.compilation.dto.CompilationDto;
import ru.yandex.practicum.compilation.service.CompilationServiceImpl;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationPublicController {
    @Autowired
    private final CompilationServiceImpl compilationService;

    @GetMapping
    public Collection<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") Long from,
            @RequestParam(defaultValue = "10") Long size
    ) {
        log.info("GET/compilations: Получить подборки pinned={}, from={}, size={}", pinned, from, size);

        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable @Positive Long compId) {
        log.info("GET/compilations/{}: Получить подборку", compId);

        return compilationService.getCompilationById(compId);
    }
}
