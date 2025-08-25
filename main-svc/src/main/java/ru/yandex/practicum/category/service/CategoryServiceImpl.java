package ru.yandex.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.category.dto.CategoryDto;
import ru.yandex.practicum.category.dto.CategoryMapper;
import ru.yandex.practicum.category.dto.NewCategoryDto;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.category.model.CategoryService;
import ru.yandex.practicum.category.repository.CategoryRepository;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.exception.model.ConflictException;
import ru.yandex.practicum.exception.model.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto add(NewCategoryDto postDto) {
        checkByName(postDto.getName());
        CategoryDto dto = CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategory(postDto)));
        log.info("Категория с id={}, name={} добавлена", dto.getId(), dto.getName());
        return dto;
    }

    @Override
    public CategoryDto update(Long id, NewCategoryDto postDto) {
        Category category = getByIdOrException(id);

        if (category.getName().equals(postDto.getName())) {
            log.info("Для категории с id={} передано то же название. Обновление не требуется", id);
            return CategoryMapper.toCategoryDto(category);
        }

        checkByName(postDto.getName());
        category.setName(postDto.getName());
        category = categoryRepository.save(category);
        log.info("Для категории с id={} название изменено на {}", id, category.getName());

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public void delete(Long id) {
        Category category = getByIdOrException(id);

        if (eventRepository.findFirstByCategoryId(id).isPresent()) {
            throw new ConflictException("Категория с id=" + id + " используется в событиях");
        }

        categoryRepository.delete(category);
        log.info("Категория с id={} удалена", id);
    }

    @Override
    public List<CategoryDto> getAll(int from, int size) {
        List<Category> list = categoryRepository.findAll(PageRequest.of(from / size, size)).getContent();
        log.info("Список категорий с from={}, size={} прочитан", from, size);
        return list.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = getByIdOrException(id);
        log.info("Категория с id={} прочитана", id);
        return CategoryMapper.toCategoryDto(category);
    }

    private Category getByIdOrException(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category with id=" + id + " was not found")
        );
    }

    private void checkByName(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new ConflictException("Категория с названием " + name + " уже существует");
        }
    }
}
