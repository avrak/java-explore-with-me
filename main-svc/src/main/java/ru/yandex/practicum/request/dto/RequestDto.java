package ru.yandex.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private String status;
}
