package ru.yandex.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.location.dto.LocationDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank(message = "Field: annotation. Error: must not be blank. Value: null")
    @Size(min = 20, max = 2000, message = "Field: annotation. Error: must be 20..2000 symbols")
    private String annotation;

    @PositiveOrZero(message = "Field: category. Error: must not be blank. Value: null")
    private Long category;

    @NotBlank(message = "Field: description. Error: must not be blank. Value: null")
    @Size(min = 20, max = 7000, message = "description: annotation. Error: must be 20..7000 symbols")
    private String description;

    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    @Size(min = 3, max = 120, message = "description: annotation. Error: must be 3..120 symbols")
    private String title;

    @NotBlank(message = "Field: eventDate. Error: must not be blank. Value: null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;

    @NotNull(message = "Field: location. Error: must not be blank. Value: null")
    private LocationDto location;

    @Builder.Default
    private boolean paid = false;

    @Builder.Default
    @PositiveOrZero(message = "Participant limit must be zero or positive")
    private int participantLimit = 0;

    @Builder.Default
    private boolean requestModeration = true;

}
