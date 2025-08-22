package ru.yandex.practicum.compilation.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationDto {
    private List<Long> events;
    private Boolean pinned;

    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    private String title;
}
