package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.exeptions.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;


    @Override
    public ItemDto create(Item item, Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            item.setOwner(userRepository.findById(userId).get());
            Item createItem = itemRepository.save(item);
            log.info("Предмет с id = '{}' добавлен в список", createItem.getId());
            return ItemMapper.toItemDto(createItem);
        } else {
            throw new EntityNotFoundException("Такого пользователя не существует!");
        }
    }

    @Override
    public List<ItemDto> readAll(Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            log.info("Параметры поиска введены не корректно");
            throw new IllegalArgumentException("Параметры поиска введены не корректно");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        return itemRepository.findAll(pageable)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemBookingDto> readAllByUserId(Long id, Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            log.info("Параметры поиска введены не корректно");
            throw new IllegalArgumentException("Параметры поиска введены не корректно");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isEmpty()) {
            throw new EntityNotFoundException("Заданного пользователя не существует");
        }
        return itemRepository
                .findAllByOwnerOrderByIdAsc(optUser.get(), pageable)
                .stream()
                .map(this::addLastAndNextBooking)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(Long id, Item item, Long userId) {
        Optional<User> optUser = userRepository.findById(userId);
        Optional<Item> optItem = itemRepository.findById(id);
        if (optUser.isEmpty()) {
            throw new EntityNotFoundException("Заданного пользователя не существует");
        }
        if (optItem.isEmpty()) {
            throw new EntityNotFoundException("Заданного предмета не существует");
        }
        Item saveItem = optItem.get();
        if (!Objects.equals(saveItem.getOwner().getId(), optUser.get().getId())) {
            throw new EntityNotFoundException("EntityNotFoundException (Предмет не может быть обновлен, т.к. он " +
                    "не принадлежит данному пользователю)");
        }
        if (item.getName() != null) {
            saveItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            saveItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            saveItem.setAvailable(item.getAvailable());
        }
        Item updateItem = itemRepository.save(saveItem);
        log.info("Предмет с id = '{}' обновлен", updateItem.getId());
        return ItemMapper.toItemDto(updateItem);
    }

    @Override
    public ItemDto getItemById(Long id) {
        Optional<Item> optItem = itemRepository.findById(id);
        if (optItem.isEmpty()) {
            throw new EntityNotFoundException("Указанного объекта не существует!");
        }
        Item item = optItem.get();
        Optional<User> optUser = userRepository.findById(item.getOwner().getId());
        if (optUser.isPresent()) {
            return ItemMapper.toItemDto(item);
        } else {
            throw new EntityNotFoundException("Такого пользователя не существует!");
        }
    }

    @Override
    public void delete(Long id, Long userId) {
        Optional<Item> optItem = itemRepository.findById(id);
        if (optItem.isPresent()) {
            itemRepository.delete(optItem.get());
        } else {
            throw new EntityNotFoundException(String.format("Предмет с id=%d отсутствует в списке", id));
        }
    }

    @Override
    public List<ItemDto> findItemsByText(String text, Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            log.info("Параметры поиска введены не корректно");
            throw new IllegalArgumentException("Параметры поиска введены не корректно");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        if (!text.isBlank()) {
            List<Item> itemsList = itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text,
                    text, pageable).toList();
            return itemsList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public CommentDto createComment(Long itemId, Long userId, Comment comment) {
        Optional<Item> optItem = itemRepository.findById(itemId);
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            throw new EntityNotFoundException("Заданного пользователя не существует");
        }
        if (optItem.isPresent()) {
            if (!bookingRepository.findAllByItemIdAndAndBooker_IdAndEndBefore(itemId, userId, LocalDateTime.now())
                    .isEmpty()) {
                comment.setItem(optItem.get());
                comment.setAuthor(optUser.get());
                comment.setCreated(LocalDateTime.now());
                return CommentMapper.toCommentDto(commentRepository.save(comment));
            } else {
                throw new IllegalArgumentException(String.format("Пользователь с id=%d не имел, либо не завершил бронь " +
                        "с предметом с id=%d", userId, itemId));
            }
        } else {
            throw new EntityNotFoundException(String.format("Предмет с id=%d отсутствует в списке", itemId));
        }
    }

    @Override
    public ItemBookingDto getItemByUserId(Long id, Long userId) {
        Optional<Item> optItem = itemRepository.findById(id);
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isEmpty()) {
            throw new EntityNotFoundException("Заданного пользователя не существует");
        }
        if (optItem.isEmpty()) {
            throw new EntityNotFoundException(String.format("Предмет с id = %d отсутствует в списке", id));
        } else {
            Item item = optItem.get();
            if (item.getOwner().getId().equals(optUser.get().getId())) {
                return addLastAndNextBooking(item);
            } else {
                List<Comment> comments = commentRepository.findAllByItem(item);
                List<CommentDto> commentsDto = new ArrayList<>();
                for (Comment comment : comments) {
                    CommentDto commentDto = CommentMapper.toCommentDto(comment);
                    commentsDto.add(commentDto);
                }
                return ItemMapper.toItemWishBookingAndCommentDto(item, null, null, commentsDto);
            }
        }
    }

    private ItemBookingDto addLastAndNextBooking(Item item) {
        ItemBookingDto itemBookingDto = ItemMapper.toItemWishBookingAndCommentDto(item, null,
                null, null);
        Booking lastBooking = bookingRepository.findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(item.getId(),
                LocalDateTime.now(), Status.APPROVED);
        if (lastBooking != null) {
            itemBookingDto.setLastBooking(BookingMapper.toShortDto(lastBooking));
        }
        Booking nextBooking = bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStart(item, LocalDateTime
                .now(), Status.APPROVED);
        if (nextBooking != null) {
            itemBookingDto.setNextBooking(BookingMapper.toShortDto(nextBooking));
        }
        List<Comment> comments = commentRepository.findAllByItem(item);
        List<CommentDto> commentsDto = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDto commentDto = CommentMapper.toCommentDto(comment);
            commentsDto.add(commentDto);
        }
        itemBookingDto.setComments(commentsDto);
        return itemBookingDto;
    }
}
