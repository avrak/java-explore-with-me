package ru.yandex.practicum.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
    private final String reason;

    public ConflictException(String reason) {
        super("Конфликт данных. " + reason);
        this.reason = reason;
    }
}