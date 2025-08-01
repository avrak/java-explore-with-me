package ru.yandex.practicum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsPostDto {
    private String app;

    private String uri;

    private String api;

    private LocalDateTime timestamp;

}
