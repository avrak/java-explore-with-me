package ru.yandex.practicum.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String reason;

    public NotFoundException(String reason) {
        super("Ресурс не найден. " + reason);
        this.reason = reason;
    }
}
