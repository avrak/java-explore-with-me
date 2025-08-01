package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.StatisticsGetDto;
import ru.yandex.practicum.StatisticsPostDto;
import ru.yandex.practicum.exception.ConflictException;
import ru.yandex.practicum.model.StatisticsMapper;
import ru.yandex.practicum.model.StatisticsServer;
import ru.yandex.practicum.repository.StatisticsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServerImpl implements StatisticsServer {
    private final StatisticsRepository statisticsRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveHit(StatisticsPostDto postDto) {
        statisticsRepository.save(StatisticsMapper.toStatistics(postDto));
    }

    @Override
    public List<StatisticsGetDto> getStatistics(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);

        if (startDateTime.isEqual(endDateTime) || startDateTime.isAfter(endDateTime)) {
            throw new ConflictException("Начало должно быть раньше конца");
        }

        if (uris.isEmpty()) {
            return statisticsRepository.findStatisticsByPeriod(startDateTime, endDateTime, unique);
        } else {
            return statisticsRepository.findStatisticsByPeriodAndUris(startDateTime, endDateTime, uris, unique);
        }
    }
}
