package ru.yandex.practicum.category.model;

import ru.yandex.practicum.category.dto.CategoryDto;
import ru.yandex.practicum.category.dto.CategoryPostDto;

import java.util.List;

public interface CategoryService {
    CategoryDto add(CategoryPostDto postDto);

    CategoryDto update(Long id, CategoryPostDto postDto);

    void delete(Long id);

    List<CategoryDto> getAll(Long from, Long size);

    CategoryDto getById(Long id);
}
