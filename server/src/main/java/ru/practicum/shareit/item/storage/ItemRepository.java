package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(String name, String description,
                                                                             Pageable pageable);

    Page<Item> findAllByOwnerOrderByIdAsc(User user, Pageable pageable);

    Page<Item> findAll(Pageable pageable);
}
