package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ru.yandex.practicum.exception.ErrorResponse handleParameterNotValidException(final ParameterNotValidException e) {
        return new ru.yandex.practicum.exception.ErrorResponse(
                "Ошибка ввода", e.getReason()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ru.yandex.practicum.exception.ErrorResponse handleNotFoundException(final NotFoundException e) {
        return new ErrorResponse(
                "Ресурс не найден", e.getReason()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ru.yandex.practicum.exception.ErrorResponse handleConflictException(final ConflictException e) {
        return new ErrorResponse(
                "Конфликт данных", e.getReason()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ru.yandex.practicum.exception.ErrorResponse handleForbiddenException(final ForbiddenException e) {
        return new ErrorResponse(
                "Запрещено", e.getReason()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ru.yandex.practicum.exception.ErrorResponse handleInternalServerException(final Throwable e) {
        return new ErrorResponse(
                "Внутренняя ошибка", e.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ErrorResponse(
                "Ошибка запроса",
                "Некорректно передан обязательный параметр: " + ex.getParameter()
        );
    }

}