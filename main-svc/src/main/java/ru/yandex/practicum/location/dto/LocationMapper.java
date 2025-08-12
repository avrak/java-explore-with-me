package ru.yandex.practicum.location.dto;

import ru.yandex.practicum.location.model.Location;

public class LocationMapper {

    public static Location toLocation(LocationDto locationDto) {
        return new Location(
                null,
                locationDto.getLat(),
                locationDto.getLon()
        );
    }

    public static LocationDto toDto(Location location) {
        return new LocationDto(location.getLat(), location.getLon());
    }
}
