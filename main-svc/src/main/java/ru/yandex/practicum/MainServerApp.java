package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.yandex.practicum", "ru.practicum"})
public class MainServerApp {
    public static void main(String[] args) {
        SpringApplication.run(MainServerApp.class, args);
    }
}