package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.StatisticsGetDto;
import ru.yandex.practicum.StatisticsPostDto;
import ru.yandex.practicum.service.StatisticsServerImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {

    @Mock
    private StatisticsServerImpl statService;

    @InjectMocks
    private StatisticsController statisticsController;

    @Test
    void saveHit_shouldCallServiceAndReturnCreatedStatus() {

        StatisticsPostDto postDto = new StatisticsPostDto(
                "app",
                "/uri",
                "192.168.1.1",
                LocalDateTime.now()
        );

        statisticsController.saveHit(postDto);

        verify(statService, times(1)).saveHit(postDto);
    }

    @Test
    void getStatistic_shouldReturnStatisticsFromService() {

        String start = "2023-01-01 00:00:00";
        String end = "2023-01-02 00:00:00";
        List<String> uris = List.of("/uri1", "/uri2");
        boolean unique = true;

        List<StatisticsGetDto> expectedStats = List.of(
                new StatisticsGetDto("app", "/uri1", 10L),
                new StatisticsGetDto("app", "/uri2", 5L)
        );

        when(statService.getStatistics(start, end, uris, unique)).thenReturn(expectedStats);

        List<StatisticsGetDto> result = statisticsController.getStatistic(start, end, uris, unique);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedStats, result);
        verify(statService, times(1)).getStatistics(start, end, uris, unique);
    }

    @Test
    void getStatistic_withUniqueFalse_shouldPassCorrectValueToService() {
        String start = "2023-01-01 00:00:00";
        String end = "2023-01-02 00:00:00";
        List<String> uris = List.of("/uri1");

        List<StatisticsGetDto> expectedStats = List.of(
                new StatisticsGetDto("app", "/uri1", 20L)
        );

        when(statService.getStatistics(start, end, uris, false)).thenReturn(expectedStats);

        List<StatisticsGetDto> result = statisticsController.getStatistic(start, end, uris, false);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(statService).getStatistics(start, end, uris, false);
    }
}