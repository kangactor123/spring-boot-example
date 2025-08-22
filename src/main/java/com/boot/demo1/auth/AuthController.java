package com.boot.demo1.auth;

import com.boot.demo1.auth.dto.LoginRequest;
import com.boot.demo1.auth.dto.LoginResponse;
import com.boot.demo1.auth.dto.SignupRequest;
import com.boot.demo1.auth.service.AuthService;
import com.boot.demo1.user.dto.User;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private  final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@CookieValue("refreshToken") String refreshToken,
                                                 HttpServletResponse response) {
        return ResponseEntity.ok(authService.refreshAccessToken(refreshToken, response));
    }

}
