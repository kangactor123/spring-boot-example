package com.boot.demo1.auth.service;

import com.boot.demo1.auth.dto.LoginRequest;
import com.boot.demo1.auth.dto.LoginResponse;
import com.boot.demo1.auth.dto.SignupRequest;
import com.boot.demo1.security.JwtUtil;
import com.boot.demo1.user.dto.User;
import com.boot.demo1.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public User signUp(SignupRequest request) {
        userRepository.findByUsername(request.userName())
                .orElseThrow(() -> new RuntimeException("이미 생성된 유저입니다."));

        return User.builder()
                .username(request.userName())
                .role(request.role())
                .password(request.password())
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.userName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!request.password().equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refreshAccessToken(String refreshToken, HttpServletResponse response) {
        if (!jwtUtil.validateToken(refreshToken)) {
            // Refresh Token invalid → 쿠키 삭제
            Cookie cookie = new Cookie("refreshToken", null);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(0); // 만료 처리
            response.addCookie(cookie);

            throw new RuntimeException("Invalid refresh token"); // 글로벌 예외 처리로 401 반환 가능
        }

        String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return new LoginResponse(newAccessToken, refreshToken);
    }
}
