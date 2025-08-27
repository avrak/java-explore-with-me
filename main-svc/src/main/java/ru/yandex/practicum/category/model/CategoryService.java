package ru.yandex.practicum.category.model;

import ru.yandex.practicum.category.dto.CategoryDto;
import ru.yandex.practicum.category.dto.NewCategoryDto;

import java.util.Collection;

public interface CategoryService {
    CategoryDto add(NewCategoryDto postDto);

    CategoryDto update(Long id, NewCategoryDto postDto);

    void delete(Long id);

    Collection<CategoryDto> getAll(int from, int size);

    CategoryDto getById(Long id);
}
