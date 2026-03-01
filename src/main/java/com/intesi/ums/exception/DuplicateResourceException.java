package com.intesi.ums.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String field, String value) {
        super("A user with " + field + " '" + value + "' already exists");
    }
}
