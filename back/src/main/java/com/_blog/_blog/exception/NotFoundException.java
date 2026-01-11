package com._blog._blog.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException{
    private final String field;
    public NotFoundException(String field, String message) {
        super(message);
        this.field = field;
    }
}