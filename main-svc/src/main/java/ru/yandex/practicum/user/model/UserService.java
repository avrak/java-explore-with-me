package ru.yandex.practicum.user.model;

import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserPostDto;

import java.util.Collection;
import java.util.List;

public interface UserService {

    UserDto addUser(UserPostDto userPostDto);

    UserDto updateUser(Long userId, UserDto user);

    Collection<UserDto> getUserList(List<Long> idList, Long from, Long size);

    void deleteUser(Long userId);

    UserDto getUserById(Long userId);

}
