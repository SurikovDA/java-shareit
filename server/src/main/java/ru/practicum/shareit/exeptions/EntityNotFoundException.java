package ru.practicum.shareit.exeptions;

import lombok.Generated;

@Generated
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
