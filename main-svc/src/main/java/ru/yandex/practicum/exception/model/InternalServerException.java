package ru.yandex.practicum.exception.model;

import lombok.Getter;

@Getter
public class InternalServerException extends RuntimeException {
    private final String reason;

    public InternalServerException(String reason) {
            super("Внутренняя ошибка. " + reason);
            this.reason = reason;
        }
}