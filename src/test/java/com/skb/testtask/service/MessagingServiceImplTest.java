package com.skb.testtask.service;

import com.skb.testtask.model.ResponseMessage;
import com.skb.testtask.model.User;
import com.skb.testtask.model.entity.UserEntity;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
class MessagingServiceImplTest {
    @Autowired
    MessagingServiceImpl messagingService;

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
        Message<ResponseMessage> response = messagingService.doRequest(any(Message.class));
        assertNotNull(response);
        assertNotNull(response.getPayload().getMessageStatus());
    }
}