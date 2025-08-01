package ru.yandex.practicum.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class StatisticsTest {

    @Test
    void testNoArgsConstructor() {
        Statistics statistics = new Statistics();

        assertNull(statistics.getId());
        assertNull(statistics.getApp());
        assertNull(statistics.getUri());
        assertNull(statistics.getIp());
        assertNull(statistics.getTimestamp());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        Statistics statistics = new Statistics(1L, "test-app", "/test-uri", "test-ip", now);

        assertEquals(1L, statistics.getId());
        assertEquals("test-app", statistics.getApp());
        assertEquals("/test-uri", statistics.getUri());
        assertEquals("test-ip", statistics.getIp());
        assertEquals(now, statistics.getTimestamp());
    }

    @Test
    void testSettersAndGetters() {
        Statistics statistics = new Statistics();
        LocalDateTime now = LocalDateTime.now();

        statistics.setId(2L);
        statistics.setApp("another-app");
        statistics.setUri("/another-uri");
        statistics.setIp("another-ip");
        statistics.setTimestamp(now);

        assertEquals(2L, statistics.getId());
        assertEquals("another-app", statistics.getApp());
        assertEquals("/another-uri", statistics.getUri());
        assertEquals("another-ip", statistics.getIp());
        assertEquals(now, statistics.getTimestamp());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime now = LocalDateTime.now();
        Statistics stats1 = new Statistics(1L, "app", "/uri", "ip", now);
        Statistics stats2 = new Statistics(1L, "app", "/uri", "ip", now);
        Statistics stats3 = new Statistics(2L, "different", "/different", "different", now.plusDays(1));

        assertEquals(stats1, stats2);
        assertNotEquals(stats1, stats3);
        assertEquals(stats1.hashCode(), stats2.hashCode());
        assertNotEquals(stats1.hashCode(), stats3.hashCode());
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        Statistics statistics = new Statistics(1L, "test-app", "/test", "test-ip", now);

        String toStringResult = statistics.toString();

        assertTrue(toStringResult.contains("id=1"));
        assertTrue(toStringResult.contains("app=test-app"));
        assertTrue(toStringResult.contains("uri=/test"));
        assertTrue(toStringResult.contains("ip=test-ip"));
        assertTrue(toStringResult.contains("timestamp=" + now));
    }
}