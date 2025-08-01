package ru.yandex.practicum.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.yandex.practicum.StatisticsGetDto;
import ru.yandex.practicum.model.Statistics;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StatisticsRepositoryTest {

    @Autowired
    private StatisticsRepository statisticsRepository;

    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime start = now.minusDays(1);
    private final LocalDateTime end = now.plusDays(1);

    @BeforeEach
    void beforeEach() {
        statisticsRepository.save(new Statistics(null, "app1", "/uri1", "192.168.1.0", now.minusMinutes(60)));
        statisticsRepository.save(new Statistics(null, "app1", "/uri1", "192.168.1.0", now.minusMinutes(20))); // Дубликат IP
        statisticsRepository.save(new Statistics(null, "app1", "/uri2", "192.168.1.2", now.minusMinutes(10)));
        statisticsRepository.save(new Statistics(null, "app2", "/uri1", "192.168.1.3", now.minusMinutes(5)));
    }

    @Test
    void findStatisticsByPeriod_shouldReturnAllHits() {
        List<StatisticsGetDto> result = statisticsRepository.findStatisticsByPeriod(start, end, false);

        assertEquals(3, result.size());
        assertStatisticsDtoExists(result, "app1", "/uri1", 2L);
        assertStatisticsDtoExists(result, "app1", "/uri2", 1L);
        assertStatisticsDtoExists(result, "app2", "/uri1", 1L);
    }

    @Test
    void findStatisticsByPeriod_withUniqueTrue_shouldReturnUniqueHits() {
        List<StatisticsGetDto> result = statisticsRepository.findStatisticsByPeriod(start, end, true);

        assertEquals(3, result.size());
        assertStatisticsDtoExists(result, "app1", "/uri1", 1L); // Один уникальный IP
        assertStatisticsDtoExists(result, "app1", "/uri2", 1L);
        assertStatisticsDtoExists(result, "app2", "/uri1", 1L);
    }

    @Test
    void findStatisticsByPeriodAndUris_shouldFilterByUris() {
        List<String> uris = List.of("/uri1", "/uri2");

        List<StatisticsGetDto> result = statisticsRepository.findStatisticsByPeriodAndUris(start, end, uris, false);

        assertEquals(3, result.size());
        assertStatisticsDtoExists(result, "app1", "/uri1", 2L);
        assertStatisticsDtoExists(result, "app1", "/uri2", 1L);
        assertStatisticsDtoExists(result, "app2", "/uri1", 1L);
    }

    @Test
    void findStatisticsByPeriodAndUris_withUniqueTrue_shouldFilterByUrisAndCountUnique() {
        List<String> uris = List.of("/uri1");

        List<StatisticsGetDto> result = statisticsRepository.findStatisticsByPeriodAndUris(start, end, uris, true);

        assertEquals(2, result.size());
        assertStatisticsDtoExists(result, "app1", "/uri1", 1L); // Один уникальный IP
        assertStatisticsDtoExists(result, "app2", "/uri1", 1L);
    }

    @Test
    void findStatisticsByPeriodAndUris_withEmptyUris_shouldReturnEmptyList() {
        List<StatisticsGetDto> result = statisticsRepository.findStatisticsByPeriodAndUris(start, end, List.of(), false);

        assertTrue(result.isEmpty());
    }

    @Test
    void findStatisticsByPeriod_withNoData_shouldReturnEmptyList() {
        List<StatisticsGetDto> result = statisticsRepository.findStatisticsByPeriod(
                LocalDateTime.now().minusDays(1000L),
                LocalDateTime.now().minusDays(900L),
                false);

        assertTrue(result.isEmpty());
    }

    private void assertStatisticsDtoExists(List<StatisticsGetDto> dtos, String app, String uri, Long hits) {
        assertTrue(dtos.stream()
                .anyMatch(dto -> dto.getApp().equals(app)
                        && dto.getUri().equals(uri)
                        && dto.getHits().equals(hits)));
    }
}