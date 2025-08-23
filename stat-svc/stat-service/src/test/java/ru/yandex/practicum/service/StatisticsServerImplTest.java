package ru.yandex.practicum.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.StatisticsGetDto;
import ru.yandex.practicum.StatisticsPostDto;
import ru.yandex.practicum.exception.ParameterNotValidException;
import ru.yandex.practicum.model.Statistics;
import ru.yandex.practicum.repository.StatisticsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServerImplTest {

    @Mock
    private StatisticsRepository statisticsRepository;

    @InjectMocks
    private StatisticsServerImpl statisticsService;

    private final String start = "2023-01-01 00:00:00";
    private final String end = "2023-01-02 00:00:00";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
    private final LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);

    @Test
    void saveHit_shouldCallRepositorySave() {
        StatisticsPostDto postDto = new StatisticsPostDto(
                "test-app",
                "/test-uri",
                "192.168.1.1",
                startDateTime
        );
        Statistics expectedStat = new Statistics(
                null,
                "test-app",
                "/test-uri",
                "192.168.1.1",
                startDateTime
        );

        statisticsService.saveHit(postDto);

        verify(statisticsRepository).save(expectedStat);
    }

    @Test
    void getStatistics_withEmptyUris_shouldCallFindStatisticsByPeriod() {
        List<StatisticsGetDto> expected = List.of(
                new StatisticsGetDto("app1", "/uri1", 10L)
        );
        when(statisticsRepository.findStatisticsByPeriod(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                anyBoolean()))
                .thenReturn(expected);

        List<StatisticsGetDto> result = statisticsService.getStatistics(
                start, end, List.of(), false);

        assertEquals(1, result.size());
        verify(statisticsRepository).findStatisticsByPeriod(
                startDateTime.minusSeconds(3),
                endDateTime,
                false);
    }

    @Test
    void getStatistics_withUris_shouldCallFindStatisticsByPeriodAndUris() {
        List<String> uris = List.of("/uri1", "/uri2");
        List<StatisticsGetDto> expected = List.of(
                new StatisticsGetDto("app1", "/uri1", 5L),
                new StatisticsGetDto("app1", "/uri2", 3L)
        );
        when(statisticsRepository.findStatisticsByPeriodAndUris(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                anyList(),
                anyBoolean()))
                .thenReturn(expected);

        List<StatisticsGetDto> result = statisticsService.getStatistics(
                start, end, uris, true);

        assertEquals(2, result.size());
        verify(statisticsRepository).findStatisticsByPeriodAndUris(
                startDateTime.minusSeconds(3),
                endDateTime,
                uris,
                true);
    }

    @Test
    void getStatistics_withEqualStartEnd_shouldThrowConflictException() {
        assertThrows(ParameterNotValidException.class, () ->
                statisticsService.getStatistics(start, start, List.of(), false));
    }

    @Test
    void getStatistics_withStartAfterEnd_shouldThrowConflictException() {
        assertThrows(ParameterNotValidException.class, () ->
                statisticsService.getStatistics(end, start, List.of(), false));
    }
}