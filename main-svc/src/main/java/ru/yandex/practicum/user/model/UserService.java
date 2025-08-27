package ru.yandex.practicum.user.model;

import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.NewUserRequestDto;

import java.util.Collection;
import java.util.List;

public interface UserService {

    UserDto addUser(NewUserRequestDto newUserRequestDto);

    UserDto updateUser(Long userId, UserDto user);

    UserDto getUserById(Long userId);

    Collection<UserDto> getUserList(List<Long> idList, Integer from, Integer size);

    void deleteUser(Long userId);


}
