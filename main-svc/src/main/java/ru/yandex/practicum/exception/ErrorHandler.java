package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.model.ErrorResponse;
import ru.yandex.practicum.exception.model.ParameterNotValidException;
import ru.yandex.practicum.exception.model.NotFoundException;
import ru.yandex.practicum.exception.model.ConflictException;
import ru.yandex.practicum.exception.model.ForbiddenException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParameterNotValidException(final ParameterNotValidException e) {
        return new ErrorResponse(
                "Ошибка ввода", e.getReason()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        return new ErrorResponse(
                "Ресурс не найден", e.getReason()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        return new ErrorResponse(
                "Конфликт данных", e.getReason()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(final ForbiddenException e) {
        return new ErrorResponse(
                "Запрещено", e.getReason()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerException(final Throwable e) {
        return new ErrorResponse(
                "Внутренняя ошибка", e.getMessage()
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingParams(MissingServletRequestParameterException ex) {
        return new ErrorResponse(
                "Ошибка запроса",
                "Не передан обязательный параметр: " + ex.getParameterName()
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