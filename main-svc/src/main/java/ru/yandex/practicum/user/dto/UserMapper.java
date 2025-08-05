package ru.yandex.practicum.user.dto;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.user.model.User;

@UtilityClass
public class UserMapper {
    public User toUser(UserPostDto postDto) {
        return new User(
                null,
                postDto.getName(),
                postDto.getEmail()
        );
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }
}
