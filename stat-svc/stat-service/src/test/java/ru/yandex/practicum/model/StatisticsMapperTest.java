package ru.yandex.practicum.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.StatisticsGetDto;
import ru.yandex.practicum.StatisticsPostDto;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class StatisticsMapperTest {

    @Test
    void toGetDto_shouldConvertStatisticsToGetDto() {
        // Arrange
        Statistics statistics = new Statistics();
        statistics.setApp("test-app");
        statistics.setUri("/test-uri");
        Long hits = 10L;

        // Act
        StatisticsGetDto result = StatisticsMapper.toGetDto(statistics, hits);

        // Assert
        assertNotNull(result);
        assertEquals("test-app", result.getApp());
        assertEquals("/test-uri", result.getUri());
        assertEquals(10L, result.getHits());
    }

    @Test
    void toStatistics_shouldConvertPostDtoToStatistics() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        StatisticsPostDto postDto = new StatisticsPostDto(
                "test-app",
                "/test-uri",
                "192.168.1.1",
                timestamp
        );

        // Act
        Statistics result = StatisticsMapper.toStatistics(postDto);

        // Assert
        assertNotNull(result);
        assertNull(result.getId()); // ID должен быть null для новой сущности
        assertEquals("test-app", result.getApp());
        assertEquals("/test-uri", result.getUri());
        assertEquals("192.168.1.1", result.getIp());
        assertEquals(timestamp, result.getTimestamp());
    }
}