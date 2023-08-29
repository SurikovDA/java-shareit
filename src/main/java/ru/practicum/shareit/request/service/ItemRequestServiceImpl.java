package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.EntityNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestWithAnswersDto;
import ru.practicum.shareit.request.dto.ItemRequestWithoutAnswersDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;

    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequest create(ItemRequestWithoutAnswersDto itemRequestDto, Long userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            log.info("Такого пользователя не существует");
            throw new EntityNotFoundException("Такого пользователя не существует");
        }
        itemRequestDto.setRequestor(UserMapper.toUserDto(optUser.get()));
        ItemRequest itemRequest = RequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequest itemRequestCreated = itemRequestRepository.save(itemRequest);
        log.info("Запрос с id = '{}' добавлен в список", itemRequestCreated.getId());
        return itemRequestCreated;
    }

    @Override
    public List<ItemRequestWithAnswersDto> getAllByRequestorId(Long userId, Integer from, Integer size) {
        if (userRepository.findById(userId).isEmpty()) {
            log.info("Пользователь отсутствует в списке");
            throw new EntityNotFoundException(String.format("Пользователь с id=%d отсутствует в списке", userId));
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestor_IdOrderByCreatedDesc(userId,
                pageable).toList();
        return itemRequests
                .stream()
                .map(RequestMapper::toItemRequestWithAnswersDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestWithAnswersDto> getAll(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemRequest> itemRequests = itemRequestRepository
                .findAllByRequestorIdNotOrderByCreatedDesc(userId, pageable).toList();
        return itemRequests
                .stream()
                .map(RequestMapper::toItemRequestWithAnswersDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestWithAnswersDto getById(Long userId, Long id) {
        if (userRepository.findById(userId).isEmpty()) {
            log.info("Такого пользователя не существует");
            throw new EntityNotFoundException("Такого пользователя не существует");
        }
        Optional<ItemRequest> optItemRequest = itemRequestRepository.findById(id);
        if (optItemRequest.isEmpty()) {
            log.info("Такого запроса не существует");
            throw new EntityNotFoundException("Такого запроса не существует");
        }
        return RequestMapper
                .toItemRequestWithAnswersDto(optItemRequest.get());
    }
}
