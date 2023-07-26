package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

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

    @Override
    public ItemDto create(Item item, Long userId) {
        Item createItem = itemRepository.create(item, userRepository.getUserById(userId));
        log.info("Предмет с id = '{}' добавлен в список", createItem.getId());
        return ItemMapper.toItemDto(createItem);
    }

    @Override
    public List<ItemDto> readAll() {
        return itemRepository.readAll()
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> readAllByUserId(Long id) {
        List<Item> itemsList = itemRepository.readAllByUserId(id);
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemsList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }

    @Override
    public ItemDto update(Long id, Item item, Long userId) {
        User user = userRepository.getUserById(userId);
        Item item1 = itemRepository.getItemById(id);
        if (!Objects.equals(item1.getOwner().getId(), user.getId())) {
            throw new EntityNotFoundException("EntityNotFoundException (Предмет не может быть обновлен, т.к. он " +
                    "не принадлежит данному пользователь)");
        }
        Item updateItem = itemRepository.update(item1.getId(), item, user);
        log.info("Предмет с id = '{}' обновлен", updateItem.getId());
        return ItemMapper.toItemDto(updateItem);
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.getItemById(id);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public void delete(Long id, Long userId) {
        if (itemRepository.getItemById(id) != null) {
            itemRepository.delete(id);
        } else {
            throw new EntityNotFoundException(String.format("Предмет с id=%d отсутствует в списке", id));
        }
    }

    @Override
    public List<ItemDto> findItemsByText(String text) {
        List<Item> itemsList = itemRepository.findItemsByText(text);
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (Item item : itemsList) {
            itemDtoList.add(ItemMapper.toItemDto(item));
        }
        return itemDtoList;
    }
}
