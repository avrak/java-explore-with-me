package ru.yandex.practicum.category.dto;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.category.model.Category;

@UtilityClass
public class CategoryMapper {
    public Category toCategory(NewCategoryDto postDto) {
        return new Category(
                null,
                postDto.getName()
        );
    }

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}
