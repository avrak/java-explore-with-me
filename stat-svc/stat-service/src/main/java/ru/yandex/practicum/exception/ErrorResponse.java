package ru.yandex.practicum.exception;

public class ErrorResponse {
    private String error;
    private String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}
