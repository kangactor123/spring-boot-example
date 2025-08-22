package com.boot.demo1.common.dto;


public record ErrorResponse(
        int status,
        String code,
        String message
) {}

