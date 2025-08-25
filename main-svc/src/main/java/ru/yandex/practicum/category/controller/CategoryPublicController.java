package ru.yandex.practicum.category.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.category.dto.CategoryDto;
import ru.yandex.practicum.category.service.CategoryServiceImpl;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryPublicController {
    @Autowired
    private final CategoryServiceImpl categoryService;

    @GetMapping
    public Collection<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("GET/categories: Получить категории from={}, size={}", from, size);
        return categoryService.getAll(from, size);
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable("id") @Positive Long id) {
        log.info("GET/categories/{}: Получить категорию", id);
        return categoryService.getById(id);
    }
}
