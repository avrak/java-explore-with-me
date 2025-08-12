package ru.yandex.practicum.compilation.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.DeclareError;
import org.hibernate.query.criteria.JpaConflictUpdateAction;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.compilation.dto.CompilationDto;
import ru.yandex.practicum.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.compilation.dto.UpdateCompilationDto;
import ru.yandex.practicum.compilation.model.CompilationService;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(NewCompilationDto newDto) {
        log.info("POST/admin/compilations: Добавить подборку {}", newDto);

        return compilationService.addCompilation(newDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive Long compId) {
        log.info("DELETE/admin/compilations/{}: Удалить подборку", compId);

        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(
            @PathVariable @Positive Long compId,
            @RequestBody UpdateCompilationDto compDto
    ) {
        log.info("PATCH/admin/compilations/{}: Изменить подборку {}", compId, compDto.toString());

        return compilationService.updateCompilation(compId, compDto);
    }
}
