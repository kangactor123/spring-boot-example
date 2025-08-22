package com.boot.demo1.security;

import lombok.Getter;

@Getter
public class JwtAuthException extends RuntimeException {
    private final String code;

    public JwtAuthException(String code, String message) {
        super(message);
        this.code = code;
    }

}