package com.todo.auth.exceptions;

public class NewPasswordSameAsOldException extends RuntimeException{
    public NewPasswordSameAsOldException(String message) {
        super(message);
    }

}
