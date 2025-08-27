package ru.yandex.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.yandex.practicum.StatClient;

@Configuration
public class StatsClientConfig {

    @Value("${stats-server.url:http://localhost:9090}")
    private String serverUrl;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .build();
    }

    @Bean
    public StatClient statClient(RestTemplate restTemplate) {
        return new StatClient(restTemplate);
    }
}