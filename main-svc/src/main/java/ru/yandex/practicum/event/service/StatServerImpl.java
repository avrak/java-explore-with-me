package ru.yandex.practicum.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import ru.yandex.practicum.StatClient;
import ru.yandex.practicum.StatisticsGetDto;
import ru.yandex.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServerImpl {
    StatClient statClient;

    public Map<Long, Integer> getViews(List<Event> events) throws JsonProcessingException {
        Map<Long, Integer> views = new HashMap<>();

        Optional<LocalDateTime> earliest = events
                .stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);

        if (earliest.isEmpty()) {
            return views;
        }

        List<String> uris = events
                .stream()
                .map(event -> "/events/" + event.getId())
                .toList();

        ResponseEntity<Object> response = statClient.getStat(earliest.get(), LocalDateTime.now(), uris, false);

        ObjectMapper mapper = new ObjectMapper();

        List<StatisticsGetDto> statisticsGetDtoList = mapper.readValue(
                mapper.writeValueAsString(response.getBody()),
                new TypeReference<>(){}
        );

        statisticsGetDtoList.forEach(statisticsGetDto -> );
        for (StatisticsGetDto statisticsGetDto : statisticsGetDtoList) {
            String[] uri = statisticsGetDto.getUri().split("/");
            if (uri.length >= 2) {

            }
        }

    }
}
