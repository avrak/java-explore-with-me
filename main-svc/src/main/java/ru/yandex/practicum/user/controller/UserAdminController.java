package ru.yandex.practicum.user.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.user.dto.NewUserRequestDto;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.model.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    UserService userService;

    @GetMapping
    public Collection<UserDto> getUserList(@RequestParam(required = false) List<Long> ids,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("GET/admin/users: Прочитать список пользователей ids={}, from={}, size={}", ids, from, size);

        return userService.getUserList(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Validated NewUserRequestDto newDto) {
        log.info("POST/admin/users: Сохранить пользователя {}", newDto);

        return userService.addUser(newDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable @Positive Long userId) {
        log.info("DELETE/admin/users/{}: Удалить пользователя", userId);

        userService.deleteUser(userId);
    }
}
