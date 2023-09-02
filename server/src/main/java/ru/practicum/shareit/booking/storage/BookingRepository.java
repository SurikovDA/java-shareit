package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByBookerIdOrderByStartDesc(Long id, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime time1,
                                                                             LocalDateTime time2, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderById(Long id, LocalDateTime time1,
                                                                             LocalDateTime time2, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime time, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long id, LocalDateTime time, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long id, Status status, Pageable pageable);

    Page<Booking> findAllByItem_OwnerIdOrderByStartDesc(Long id, Pageable pageable);

    Page<Booking> findAllByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime time1,
                                                                                 LocalDateTime time2, Pageable pageable);

    Page<Booking> findAllByItem_OwnerIdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime time, Pageable pageable);

    Page<Booking> findAllByItem_OwnerIdAndStartAfterOrderByStartDesc(Long id, LocalDateTime time, Pageable pageable);

    Page<Booking> findAllByItem_OwnerIdAndStatusOrderByStartDesc(Long id, Status status, Pageable pageable);

    List<Booking> findAllByItemIdAndAndBooker_IdAndEndBefore(Long itemId, Long userId, LocalDateTime
            time);

    Booking findFirstByItemAndStartAfterAndStatusOrderByStart(Item item, LocalDateTime today, Status status);

    Booking findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(Long itemId, LocalDateTime date, Status status);
}
