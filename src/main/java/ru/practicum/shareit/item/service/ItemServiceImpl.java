package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.practicum.shareit.exeptions.ValidationException;
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
    public List<ItemDto> readAll() {
        return itemRepository.findAll()
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemBookingDto> readAllByUserId(Long id) {
        return itemRepository
                .findAllByOwner(userRepository.findById(id).get())
                .stream()
                .sorted((o1, o2) -> o1.getId()
                        .compareTo(o2.getId()))
                .map(this::addLastAndNextBooking)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto update(Long id, Item item, Long userId) {
        User user = userRepository.findById(userId).get();
        Item item1 = itemRepository.findById(id).get();
        if (!Objects.equals(item1.getOwner().getId(), user.getId())) {
            throw new EntityNotFoundException("EntityNotFoundException (Предмет не может быть обновлен, т.к. он " +
                    "не принадлежит данному пользователю)");
        }
        if (item.getName() != null) {
            itemRepository.findById(id).get().setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemRepository.findById(id).get().setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemRepository.findById(id).get().setAvailable(item.getAvailable());
        }
        Item updateItem = itemRepository.save(itemRepository.findById(id).get());
        log.info("Предмет с id = '{}' обновлен", updateItem.getId());
        return ItemMapper.toItemDto(updateItem);
    }

    @Override
    public ItemDto getItemById(Long id) {
        if (itemRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Указанного объекта не существует!");
        }
        Item item = itemRepository.findById(id).get();
        if (userRepository.findById(item.getOwner().getId()).isPresent()) {
            return ItemMapper.toItemDto(item);
        } else {
            throw new EntityNotFoundException("Такого пользователя не существует!");
        }
    }

    @Override
    public void delete(Long id, Long userId) {
        if (itemRepository.findById(id).isPresent()) {
            itemRepository.delete(itemRepository.findById(id).get());
        } else {
            throw new EntityNotFoundException(String.format("Предмет с id=%d отсутствует в списке", id));
        }
    }

    @Override
    public List<ItemDto> findItemsByText(String text) {
        if (!text.isBlank()) {
            List<Item> itemsList = itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, text);
            List<ItemDto> itemDtoList = new ArrayList<>();
            for (Item item : itemsList) {
                itemDtoList.add(ItemMapper.toItemDto(item));
            }
            return itemDtoList;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public CommentDto createComment(Long itemId, Long userId, Comment comment) {
        if (itemRepository.findById(itemId).isPresent()) {
            if (!bookingRepository.findAllByItemIdAndAndBooker_IdAndEndBefore(itemId, userId, LocalDateTime.now())
                    .isEmpty()) {
                comment.setItem(itemRepository.findById(itemId).get());
                comment.setAuthor(userRepository.findById(userId).get());
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
        if (itemRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException(String.format("Предмет с id = %d отсутствует в списке", id));
        } else {
            Item item = itemRepository.findById(id).get();
            if (item.getOwner().getId().equals(userRepository.findById(userId).get().getId())) {
                return addLastAndNextBooking(item);
            } else {
                List<Comment> comments = commentRepository.findAllByItem(itemRepository.findById(id).get());
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
        Booking lastBooking = bookingRepository.findFirstByItemIdAndStartBeforeOrderByStartDesc(item.getId(),
                LocalDateTime.now());
        if (lastBooking != null && lastBooking.getStatus().equals(Status.APPROVED)) {
            itemBookingDto.setLastBooking(BookingMapper.toShortDto(lastBooking));
        }
        Booking nextBooking = bookingRepository.findFirstByItemAndStartAfterOrderByStart(item, LocalDateTime.now());
        if (nextBooking != null && nextBooking.getStatus().equals(Status.APPROVED)) {
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

    private void validate(Item item) {
        if (item.getName().isEmpty()) {
            log.info("ValidationException (Пустое название)");
            throw new ValidationException("Пустое название");
        }
        if (item.getDescription().isEmpty()) {
            log.info("ValidationException (Пустое описание)");
            throw new ValidationException("Пустое описание");
        }
        if (item.getAvailable() == null) {
            log.info("ValidationException (Ошибка статуса предмета с id = {})", item.getId());
            throw new IllegalArgumentException("Ошибка статуса предмет");
        }
    }
}
