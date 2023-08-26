package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository requestRepository;
    @Autowired
    BookingRepository bookingRepository;

    User user;
    Item item;
    ItemRequest request;
    Booking booking;
    LocalDateTime start;
    LocalDateTime end;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(User.builder()
                .id(1L)
                .name("user1")
                .email("user1@mail.ru")
                .build());
        request = requestRepository.save(ItemRequest.builder()
                .id(1L)
                .description("description")
                .requestor(user)
                .created(LocalDateTime.now())
                .build());
        item = itemRepository.save(Item.builder()
                .id(1L)
                .name("item1")
                .description("description1")
                .available(true)
                .owner(user)
                .itemRequest(request)
                .build());
        booking = bookingRepository.save(Booking.builder()
                .id(1L)
                .booker(user)
                .status(Status.APPROVED)
                .item(item)
                .start(LocalDateTime.of(2022, 1, 1, 1, 1, 1))
                .end(LocalDateTime.of(2022, 2, 1, 1, 1, 1))
                .build());
        start = LocalDateTime.of(2021, 1, 1, 1, 1, 1);
        end = LocalDateTime.of(2023, 1, 1, 1, 1, 1);
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(1L, Pageable.unpaged()).toList();

        assertEquals(bookings.size(), 0);
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(1L,
                end, start, Pageable.unpaged()).toList();

        assertEquals(bookings.size(), 0);
    }

    @Test
    void findAllByBookerIdAndStartAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(1L,
                start, Pageable.unpaged()).toList();

        assertEquals(bookings.size(), 0);
    }

  /*  @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(1L,
                Status.APPROVED, Pageable.unpaged()).toList();
        List<Booking> bookings2 = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(1L,
                Status.WAITING, Pageable.unpaged()).toList();

        assertEquals(bookings.size(), 1);
        assertEquals(bookings2.size(), 0);
    }

    @Test
    void findAllByItem_OwnerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByItem_OwnerIdOrderByStartDesc(1L,
                Pageable.unpaged()).toList();

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(1L,
                end, start, Pageable.unpaged()).toList();

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByItem_OwnerIdAndStartAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByItem_OwnerIdAndStartAfterOrderByStartDesc(1L,
                start, Pageable.unpaged()).toList();

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByItem_OwnerIdAndStatusOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByItem_OwnerIdAndStatusOrderByStartDesc(1L,
                Status.APPROVED, Pageable.unpaged()).toList();
        List<Booking> bookings2 = bookingRepository.findAllByItem_OwnerIdAndStatusOrderByStartDesc(1L,
                Status.WAITING, Pageable.unpaged()).toList();

        assertEquals(bookings.size(), 1);
        assertEquals(bookings2.size(), 0);
    }

    @Test
    void findFirstByItemAndStartAfterAndStatusOrderByStart() {
        Booking booking1 = bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStart(item,
                start, Status.APPROVED);

        assertEquals(booking1.getItem(), item);
    }*/
}
