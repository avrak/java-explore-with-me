package ru.yandex.practicum.event.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.event.model.StateActionAdmin;
import ru.yandex.practicum.location.dto.LocationDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventUpdateAdminDto {

    @Size(min = 3, max = 120, message = "Заголовок события должен содержать от 3 до 120 символов")
    private String title;

    @Size(min = 20, max = 2000, message = "Аннотация события должна содержать от 20 до 2000 символов")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "Описание события должно содержать от 20 до 7000 символов")
    private String description;

    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateActionAdmin stateAction;
}