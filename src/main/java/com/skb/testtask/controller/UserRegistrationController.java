package com.skb.testtask.controller;
import com.skb.testtask.model.User;
import com.skb.testtask.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер с endpoint для регистрации
 */
@RestController
@RequestMapping("/registration")
public class UserRegistrationController {

    private final UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User registerUserAccount(@RequestBody User user) {
        return userService.save(user);
    }
}
