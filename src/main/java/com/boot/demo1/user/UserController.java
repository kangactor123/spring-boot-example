package com.boot.demo1.user;


import com.boot.demo1.user.dto.User;
import com.boot.demo1.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Optional<User> getUser(@RequestParam String userName) {
        return this.userService.getUser(userName);
    }
}
