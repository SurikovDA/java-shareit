package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Long id);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime time1,
                                                                             LocalDateTime time2);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime time);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long id, LocalDateTime time);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long id, Status status);

    List<Booking> findAllByItem_OwnerIdOrderByStartDesc(Long id);

    List<Booking> findAllByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime time1,
                                                                                 LocalDateTime time2);

    List<Booking> findAllByItem_OwnerIdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime time);

    List<Booking> findAllByItem_OwnerIdAndStartAfterOrderByStartDesc(Long id, LocalDateTime time);

    List<Booking> findAllByItem_OwnerIdAndStatusOrderByStartDesc(Long id, Status status);

    List<Booking> findAllByItemIdAndAndBooker_IdAndEndBefore(Long itemId, Long userId, LocalDateTime
            time);

    Booking findFirstByItemAndStartAfterOrderByStart(Item item, LocalDateTime today);

    Booking findFirstByItemIdAndStartBeforeOrderByStartDesc(Long itemId, LocalDateTime date);
}
