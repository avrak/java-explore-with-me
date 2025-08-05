package ru.yandex.practicum.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.category.model.Category;

import java.util.List;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    List<Category> findCategoryByIdBetweenFromAndTo(Long from, Long to);
}
