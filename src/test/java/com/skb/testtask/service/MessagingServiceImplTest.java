package com.skb.testtask.service;

import com.skb.testtask.model.ResponseMessage;
import com.skb.testtask.model.User;
import com.skb.testtask.model.UserConfirm;
import com.skb.testtask.model.entity.UserEntity;
import com.skb.testtask.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
class MessagingServiceImplTest {
    @Autowired
    MessagingServiceImpl messagingService;
    @MockBean
    UserRepository userRepository;
    @MockBean
    SendEmailImpl sendEmail;

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
    void send() {
    }

    @Test
    void receive() {
    }

    @Test
    void doRequest() throws TimeoutException {
        Message<User> userMessage = MessageBuilder.withPayload(user).build();
        Message<ResponseMessage> response = messagingService.doRequest(userMessage);
        assertNotNull(response);
        assertNotEquals(messagingService.getUsersWaitConfirm().size(), 0);
        assertNotNull(response.getPayload().getMessageStatus());
    }

    @Test
    void handleMessage() throws TimeoutException {
        UserConfirm userConfirm = new UserConfirm();
        userConfirm.setUserId(1L);
        userConfirm.setConfirmStatus("OK");
        Message<UserConfirm> userConfirmMessage = MessageBuilder.withPayload(userConfirm).build();
        user.setId(1L);
        user.setEmail("TEST");
        Message<User> userMessage = MessageBuilder.withPayload(user).build();
        Message<ResponseMessage> response = messagingService.doRequest(userMessage);
        assertNotNull(response);
        assertNotEquals(messagingService.getUsersWaitConfirm().size(), 0);
        Mockito.when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        Mockito.when(userRepository.findByEmail(any(String.class))).thenReturn(userEntity);
        messagingService.handleMessage(userConfirmMessage);
        Mockito.verify(userRepository, Mockito.times(1)).save(any(UserEntity.class));
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(any(String.class));
        Mockito.verify(sendEmail, Mockito.times(1)).sendMail(any(String.class),any(String.class),any(String.class));
    }
}