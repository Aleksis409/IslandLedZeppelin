package com.javarush.island.artemov.exception;

public class GameInitializationException extends RuntimeException {
    public GameInitializationException(String message) {
        super(message);
    }

    public GameInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
