package ru.yandex.practicum.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned = false;

    @NotBlank(message = "Field: title. Error: must not be blank. Value: null")
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    private String title;
}
