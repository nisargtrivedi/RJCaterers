package com.app.posapp.Utility;

public class InitializationException extends Throwable {

    public String message;

    public InitializationException(String message) {
        this.message = message;
    }
}
