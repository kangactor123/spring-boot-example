package com.boot.demo1.auth.dto;

public record LoginResponse(String accessToken, String refreshToken) {}