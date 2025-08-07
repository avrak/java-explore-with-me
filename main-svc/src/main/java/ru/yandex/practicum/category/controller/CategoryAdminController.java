package ru.yandex.practicum.category.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.category.dto.CategoryDto;
import ru.yandex.practicum.category.dto.CategoryPostDto;
import ru.yandex.practicum.category.model.CategoryService;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto save(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody CategoryPostDto postDto
    ) {
        log.info("POST/admin/categories: Сохранить категорию {} ", postDto.toString());
        return categoryService.add(postDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") @Positive Long id) {
        log.info("DELETE/admin/categories/{}: Удалить категорию", id);
        categoryService.delete(id);
    }

    @PatchMapping("/{id}")
    public CategoryDto update(@PathVariable("id") @Positive Long id,
                              @Valid @RequestBody CategoryPostDto postDto) {
        log.info("PATCH/admin/categories/{}: Изменить категорию на {}", id, postDto.toString());
        return categoryService.update(id, postDto);
    }
}
