package ru.practicum.shareit.exeptions;

import lombok.Generated;

@Generated
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
