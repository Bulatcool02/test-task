package com.skb.testtask.service;

import com.skb.testtask.model.ResponseMessage;
import com.skb.testtask.model.User;
import com.skb.testtask.model.UserConfirm;
import com.skb.testtask.model.UserStatus;
import com.skb.testtask.model.entity.UserEntity;
import com.skb.testtask.repository.UserRepository;
import com.skb.testtask.util.ErrorCode;
import com.skb.testtask.util.MasterException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Все как в предложенном вами варианте
 */
@Service
public class MessagingServiceImpl implements MessagingService, MessageListener<UserConfirm>{
    private final List<User> usersWaitConfirm =  new ArrayList<>();
    private final UserRepository userRepository;
    private final SendEmailImpl sendEmail;

    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailImpl.class);

    public MessagingServiceImpl(UserRepository userRepository, SendEmailImpl sendEmail) {
        this.userRepository = userRepository;
        this.sendEmail = sendEmail;
    }

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
        Message<ResponseMessage> response =  receive(messageId);
        if (response.getPayload().getMessageStatus().equals("OK")){
            usersWaitConfirm.add(request.getPayload());
        }
        return response;
    }

    @Override
    public void handleMessage(Message<UserConfirm> incomingMessage) {
        User deleteThisUser = null;
        for (User user:usersWaitConfirm){
            if (user.getId().equals(incomingMessage.getPayload().getUserId())){
                UserEntity userEntity = this.userRepository.findByEmail(user.getEmail());
                if (userEntity == null){
                    throw new MasterException(ErrorCode.USER_NOT_FOUND);
                }
                deleteThisUser = user;
                String content;
                if (incomingMessage.getPayload().getConfirmStatus().equals("OK")){
                    content = "good mail";
                    userEntity.setStatus(UserStatus.ACTIVE);
                } else {
                    content = "bad mail";
                    userEntity.setStatus(UserStatus.INACTIVE);
                }
                userRepository.save(userEntity);
                try{
                    sendEmail.sendMail("TEST", user.getEmail(), content);
                } catch (TimeoutException e){
                    throw new MasterException(ErrorCode.EMAIL_EXCEPTION);
                }
            }
        }
        if (deleteThisUser != null){
            usersWaitConfirm.remove(deleteThisUser);
        }
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

    public List<User> getUsersWaitConfirm() {
        return usersWaitConfirm;
    }
}
