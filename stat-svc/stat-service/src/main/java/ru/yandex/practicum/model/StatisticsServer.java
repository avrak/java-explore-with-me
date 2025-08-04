package ru.yandex.practicum.model;

import ru.yandex.practicum.StatisticsGetDto;
import ru.yandex.practicum.StatisticsPostDto;

import java.util.List;

public interface StatisticsServer {

    void saveHit(StatisticsPostDto statisticsDto);

    List<StatisticsGetDto> getStatistics(String start, String end, List<String> uriList, Boolean unique);
}
