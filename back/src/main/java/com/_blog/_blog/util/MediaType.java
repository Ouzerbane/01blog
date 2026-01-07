package com._blog._blog.util;

public enum MediaType {
    IMAGE,
    VIDEO;

   @Override
    public String toString() {
        switch (this) {
            case IMAGE:
                return "IMAGE";
            case VIDEO:
                return "VIDEO";
            default:
                return "UNKNOWN";
        }
    }
}
