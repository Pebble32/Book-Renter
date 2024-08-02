package com.adam.book.configurations.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {

    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    INCORRECT_PASSWORD(300, BAD_REQUEST, "Incorrect password"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, BAD_REQUEST, "Password does not match"),
    ACCOUNT_LOCKED(302, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(303, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(304, FORBIDDEN, "Login and / or password is incorrect"),
    ;
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatusCode;

    BusinessErrorCodes(int code, HttpStatus httpStatusCode, String description) {
        this.code = code;
        this.description = description;
        this.httpStatusCode = httpStatusCode;
    }
}
