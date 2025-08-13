package ru.yandex.practicum.request.dto;

import ru.yandex.practicum.request.model.Request;

import java.time.format.DateTimeFormatter;

public class RequestMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static RequestDto toDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getCreated().format(formatter),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus().toString()
        );
    }

//    public static Request toRequest(RequestDto requestDto) {
//        return new Request(
//                requestDto.getId(),
//                String.format(requestDto.getCreated(), formatter),
//                null,
//                null,
//                requestDto.getStatus()
//                )
//    }
}
