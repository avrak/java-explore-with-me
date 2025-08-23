package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.StatisticsGetDto;
import ru.yandex.practicum.StatisticsPostDto;
import ru.yandex.practicum.service.StatisticsServerImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {
    private final StatisticsServerImpl statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveHit(@RequestBody StatisticsPostDto postDto) {
        log.info("POST/hit: Сохранить обращение {}", postDto.toString());
        statService.saveHit(postDto);
        log.info("POST/hit: Обращение сохранено");
    }

    @GetMapping("/stats")
    public List<StatisticsGetDto> getStatistic(
            @RequestParam @Validated String start,
            @RequestParam @Validated String end,
            @RequestParam(defaultValue = "") List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique
    ) {
        log.info("GET/stats - Получить статистику uris={} from={} to={}, unique={}", uris, start, end, unique);
        return statService.getStatistics(start, end, uris, unique);
    }

}
