package com.skb.testtask.service;

import com.skb.testtask.model.ResponseMessage;
import com.skb.testtask.model.User;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Все как в предложенном вами варианте
 */
@Service
public class MessagingServiceImpl implements MessagingService{

    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailImpl.class);

    @Override
    public <T> UUID send(Message<User> msg) {
        return UUID.randomUUID();
    }

    @Override
    public <T> Message<ResponseMessage> receive(UUID messageId) throws TimeoutException {
        ResponseMessage responseMessage = new ResponseMessage();
        if(shouldThrowTimeout()) {
            sleep();
            throw new TimeoutException("Timeout!");
        }

        if(shouldSleep()) {
            sleep();
        }

        responseMessage.setMessageId(messageId);
        //рандомно генерим ответ на запрос по регистрации
        if (new Random().nextBoolean()){
            responseMessage.setMessageStatus("OK");
        } else {
            responseMessage.setMessageStatus("FAIL");
        }
        return MessageBuilder.withPayload(responseMessage).build();
    }

    @Override
    public <R, A> Message<ResponseMessage> doRequest(Message<User> request) throws TimeoutException {
        final UUID messageId = send(request);

        return receive(messageId);
    }

    @SneakyThrows
    private static void sleep() {
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }


    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }

    private static boolean shouldThrowTimeout() {
        return new Random().nextInt(10) == 1;
    }
}
