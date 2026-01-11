package com._blog._blog.exception;

import lombok.Getter;

@Getter
public class ForbiddenException  extends RuntimeException{
    private final String field;
    public ForbiddenException(String field, String message) {
        super(message);
        this.field = field;
    }
}


