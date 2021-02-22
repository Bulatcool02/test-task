package com.skb.testtask.service;

import com.skb.testtask.model.ResponseMessage;
import com.skb.testtask.model.User;
import com.skb.testtask.model.entity.UserEntity;
import com.skb.testtask.repository.UserRepository;
import com.skb.testtask.util.ErrorCode;
import com.skb.testtask.util.MasterException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder encoder;
    @MockBean
    private MessagingServiceImpl messagingService;
    @MockBean
    private SendEmailImpl sendEmailImpl;


    private final UserEntity userEntity = new UserEntity();
    private final User user = new User();

    @Before
    public void init(){
        user.setEmail("TEST");
        user.setLogin("TEST");
        user.setName("TEST");
        user.setPatronymic("TEST");
        user.setSurname("TEST");
        user.setPassword("msadfkjshd");

        userEntity.setEmail("TEST");
        userEntity.setLogin("TEST");
        userEntity.setName("TEST");
        userEntity.setPatronymic("TEST");
        userEntity.setSurname("TEST");
        userEntity.setPassword("msadfkjshd");
    }

    @Test
    void save() throws TimeoutException {
        Mockito.when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        User result = userService.save(user);
        Mockito.verify(userRepository, Mockito.times(1)).save(any(UserEntity.class));
        Mockito.verify(messagingService, Mockito.times(1)).doRequest(any(Message.class));
        assertNotNull(result);
        assertEquals(userEntity.getEmail(), result.getEmail());
    }

    @Test
    void sendUserToQueue() throws TimeoutException {
        userService.sendUserToQueue(user);
        Mockito.verify(messagingService, Mockito.times(1)).doRequest(any(Message.class));
    }
    @Test
    void sendEmail() throws TimeoutException {
        userService.sendEmail("TEST", "TEST");
        Mockito.verify(sendEmailImpl, Mockito.times(1)).sendMail("TEST@MAIL.RU", "TEST", "TEST");
    }
}