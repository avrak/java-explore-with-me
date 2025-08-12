package ru.yandex.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.model.ConflictException;
import ru.yandex.practicum.exception.model.NotFoundException;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserMapper;
import ru.yandex.practicum.user.dto.NewUserRequestDto;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.model.UserService;
import ru.yandex.practicum.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(NewUserRequestDto newUserRequestDto) {
        if (userRepository.existsByEmail(newUserRequestDto.getEmail())) {
            throw new ConflictException("Пользователь с email " + newUserRequestDto.getEmail() + " уже существует");
        }
        UserDto dto = UserMapper.toUserDto(userRepository.save(UserMapper.toUser(newUserRequestDto)));
        log.info("Пользователь с id={}, name={}, email={} добавлен", dto.getId(), dto.getName(), dto.getEmail());
        return dto;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto user) {
        User currentUser = userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));

        if (
                user.getEmail() != null
                        && !user.getEmail().equals(currentUser.getEmail())
                        && userRepository.existsByEmail(user.getEmail())
        ) {
            throw new ConflictException("Пользователь с email " + user.getEmail() + " уже существует");
        }

        if (user.getEmail() != null) {
            currentUser.setEmail(user.getEmail());
        }

        if (user.getName() != null) {
            currentUser.setName(user.getName());
        }

        UserDto dto = UserMapper.toUserDto(userRepository.save(currentUser));
        log.info("Пользователь с id={}, name={}, email={} обновлён", dto.getId(), dto.getName(), dto.getEmail());
        return dto;
    }

    @Override
    public UserDto getUserById(Long userId) {
        UserDto dto = UserMapper.toUserDto(
                userRepository.findUserById(userId).orElseThrow(
                        () -> new NotFoundException("Пользователь с id=" + userId + " не найден")
                )
        );

        log.info("Пользователь с id={} прочитан", userId);

        return dto;
    }

    @Override
    public Collection<UserDto> getUserList(List<Long> idList, Long from, Long size) {
        List<UserDto> list = (idList == null || idList.isEmpty() ?
                userRepository.findUserByIdBetweenFromAndTo(from, from + size)
                : userRepository.findAllById(idList)
        ).stream().map(UserMapper::toUserDto).toList();

        log.info("Список пользователей с idList={}, from={}, size={} прочитан", idList, from, size);

        return list;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("Пользователь с id={} удалён", userId);
    }

}
