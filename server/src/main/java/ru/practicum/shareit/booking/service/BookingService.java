package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking create(Booking booking, Long bookerId);

    Booking setStatus(Long id, Long userId, boolean approved);

    Booking get(Long id, Long userId);

    List<Booking> findAllByRenterId(Long id, State state, Integer from, Integer size);

    List<Booking> findAllByOwnerId(Long id, State state, Integer from, Integer size);
}
