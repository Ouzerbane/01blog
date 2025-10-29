package com._blog._blog.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String field;

    public CustomException(String field, String message) {
        super(message);
        this.field = field;
    }
}
