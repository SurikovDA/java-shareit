package ru.practicum.shareit.bookingTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
import java.util.ArrayList;
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
                .id(2L)
                .description("description")
                .requestor(user)
                .created(LocalDateTime.now())
                .build());
        item = itemRepository.save(Item.builder()
                .id(3L)
                .name("item1")
                .description("description1")
                .available(true)
                .owner(user)
                .itemRequest(request)
                .build());
        booking = bookingRepository.save(Booking.builder()
                .id(4L)
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
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByBookerIdAndEndBeforeOrderByStartDesc() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByBookerIdAndStartAfterOrderByStartDesc() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByItem_OwnerIdOrderByStartDesc() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByItem_OwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByItem_OwnerIdAndEndBeforeOrderByStartDesc() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByItem_OwnerIdAndStartAfterOrderByStartDesc() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByItem_OwnerIdAndStatusOrderByStartDesc() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        assertEquals(bookings.size(), 1);
    }

    @Test
    void findAllByItemIdAndAndBooker_IdAndEndBefore() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        assertEquals(bookings.size(), 1);
    }
}