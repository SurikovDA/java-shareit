package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.EntityNotFoundException;
import ru.practicum.shareit.exeptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item create(Item item, Long userId) {
        validate(item, userId);
        if (itemRepository.getItemById(item.getId()) != null) {
            throw new ValidationException("Предмет с таким id уже есть в базе");
        }
        Item createItem = itemRepository.create(item, userRepository.getUserById(userId));
        log.info("Предмет с id = '{}' добавлен в список", createItem.getId());
        return createItem;
    }

    @Override
    public List<Item> readAll() {
        return itemRepository.readAll();
    }

    @Override
    public List<Item> readAllByUserId(Long id) {
        return itemRepository.readAllByUserId(id);
    }

    @Override
    public Item update(Long id, Item item, Long userId) {
        if (itemRepository.getItemById(id).getOwner() != userRepository.getUserById(userId)) {
            throw new EntityNotFoundException("EntityNotFoundException (Предмет не может быть обновлен, т.к. он " +
                    "не принадлежит данному пользователь)");
        }
        if (itemRepository.getItemById(id) != null) {
            Item updateItem = itemRepository.update(id, item, userRepository.getUserById(userId));
            log.info("Предмет с id = '{}' обновлен", updateItem.getId());
            return updateItem;
        } else {
            log.info("EntityNotFoundException (Предмет не может быть обновлен, т.к. его нет в списке)");
            throw new EntityNotFoundException("Такого предмета не существует");
        }
    }

    @Override
    public Item getItemById(Long id) {
        if (itemRepository.getItemById(id) != null) {
            return itemRepository.getItemById(id);
        } else {
            throw new EntityNotFoundException(String.format("Предмет с id=%d отсутствует в списке", id));
        }
    }

    @Override
    public void delete(Long id, Long userId) {
        validate(getItemById(id), userId);
        if (itemRepository.getItemById(id) != null) {
            itemRepository.delete(id);
        } else {
            throw new EntityNotFoundException(String.format("Предмет с id=%d отсутствует в списке", id));
        }
    }

    @Override
    public List<Item> findItemsByText(String text) {
        return itemRepository.findItemsByText(text);
    }

    private void validate(Item item, Long userId) {
        if (userRepository.getUserById(userId) == null) {
            log.info("EntityNotFoundException (Пользователь с id = {} отсутствует в списке)", userId);
            throw new EntityNotFoundException("Пользователя не существует");
        }
        if (item.getAvailable() == null) {
            log.info("ValidationException (Ошибка статуса предмета с id = {})", item.getId());
            throw new IllegalArgumentException("Статус предмета отсутствует");
        }
    }
}
