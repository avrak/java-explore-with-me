package ru.yandex.practicum.event.dto;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.event.model.StateActionUser;
import ru.yandex.practicum.location.dto.LocationDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequestDto {
    @Size(min = 20, max = 2000, message = "Field: annotation. Error: must be 20..2000 symbols")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "description: annotation. Error: must be 20..7000 symbols")
    private String description;

    @Size(min = 3, max = 120, message = "description: annotation. Error: must be 3..120 symbols")
    private String title;

    private String eventDate;
    private LocationDto location;
    private Boolean paid;

    @PositiveOrZero(message = "Participant limit must be zero or positive")
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateActionUser stateAction;
}
