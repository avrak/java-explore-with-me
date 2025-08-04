package ru.yandex.practicum.model;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.StatisticsGetDto;
import ru.yandex.practicum.StatisticsPostDto;

@UtilityClass
public class StatisticsMapper {

  public static StatisticsGetDto toGetDto(Statistics statistics, Long hits) {
    return new StatisticsGetDto(statistics.getApp(), statistics.getUri(), hits);
  }

  public static Statistics toStatistics(StatisticsPostDto postDto) {
    return new Statistics(
            null,
            postDto.getApp(),
            postDto.getUri(),
            postDto.getIp(),
            postDto.getTimestamp()
    );
  }

}
