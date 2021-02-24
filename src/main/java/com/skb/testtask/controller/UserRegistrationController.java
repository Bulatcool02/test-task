package com.skb.testtask.controller;
import com.skb.testtask.model.User;
import com.skb.testtask.model.UserConfirm;
import com.skb.testtask.model.entity.UserEntity;
import com.skb.testtask.service.MessagingServiceImpl;
import com.skb.testtask.service.UserService;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер с endpoint для регистрации
 */
@RestController
@RequestMapping("/registration")
public class UserRegistrationController {

    private final UserService userService;
    private final MessagingServiceImpl messagingService;

    public UserRegistrationController(UserService userService, MessagingServiceImpl messagingService) {
        this.userService = userService;
        this.messagingService = messagingService;
    }

    @PostMapping
    public User registerUserAccount(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping
    public void sendResponse(@RequestParam(name = "id") Long id){
        UserConfirm userConfirm = new UserConfirm();
        userConfirm.setUserId(id);
        userConfirm.setConfirmStatus("OK");
        Message<UserConfirm> userConfirmMessage = MessageBuilder.withPayload(userConfirm).build();
        messagingService.handleMessage(userConfirmMessage);
    }

    @GetMapping("/user")
    public UserEntity getById(@RequestParam(name = "id") Long id){
        return userService.getUser(id);
    }
}
