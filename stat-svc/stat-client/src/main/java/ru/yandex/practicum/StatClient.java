package ru.yandex.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.yandex.practicum.client.BaseClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class StatClient extends BaseClient {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatClient(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public void hit(StatisticsPostDto postDto) {
        log.info("StatClient.hit: Сохранить хит={}", postDto);
        post("/hit", postDto);
        log.info("StatClient.hit: Хит сохранён");
    }

    public ResponseEntity<Object> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        final DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/stats")
                .queryParam("start", start.format(tFormatter))
                .queryParam("end", end.format(tFormatter))
                .queryParam("unique", unique.toString());

        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                builder.queryParam("uris", uri);
            }
        }

        String path = builder.toUriString();

        log.info("StatClient.getStat: path={}", path);

        return get(path);
    }

}
