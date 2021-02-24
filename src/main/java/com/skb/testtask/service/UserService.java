package com.skb.testtask.service;

import com.skb.testtask.model.ResponseMessage;
import com.skb.testtask.model.User;
import com.skb.testtask.model.UserConfirm;
import com.skb.testtask.model.UserStatus;
import com.skb.testtask.model.entity.UserEntity;
import com.skb.testtask.repository.UserRepository;
import com.skb.testtask.util.ErrorCode;
import com.skb.testtask.util.MasterException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * Сервис по обработке запросов на создание юзера
 * Все методы сделал public чтобы можно было протестировать
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendEmailImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final MessagingServiceImpl messagingService;
    private final ModelMapper modelMapper;

    private final List<User> usersErrorOnQueue = new ArrayList<>();


    public UserService(UserRepository userRepository,
                       PasswordEncoder encoder,
                       MessagingServiceImpl messagingService,
                       ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.messagingService = messagingService;
        this.modelMapper = modelMapper;
    }

    /**
     * Сохранение юзера в БД и отправка его в шину событий
     * @param user - инфо по юзеру с фронта
     * @return - созданная сущность юзера в БД
     */
    public User save(User user){
        // тут надо пароль каким то образом кодировать т.к. в идеале нигде не должно быть пароля в явном виде
        user.setPassword(encoder.encode(user.getPassword()));
        // чекаем на наличие этого юзера в БД
        // я решил что уникальность юзера должна определятся полями email и логин
        UserEntity existingEmail = userRepository.findByEmail(user.getEmail());
        UserEntity existingLogin = userRepository.findByLogin(user.getLogin());
        UserEntity userEntity = new UserEntity();
        if (existingEmail != null || existingLogin != null) {
            throw new MasterException(ErrorCode.USER_EXIST);
        } else {
            userEntity.setEmail(user.getEmail());
            userEntity.setLogin(user.getLogin());
            userEntity.setName(user.getName());
            userEntity.setPatronymic(user.getPatronymic());
            userEntity.setSurname(user.getSurname());
            // есть небольшая система статусов для юзеров
            // по умолчанию задаем статус ожидание ответа по шине по верификации
            userEntity.setStatus(UserStatus.WAIT);
        }
        UserEntity userEntityReturn = userRepository.save(userEntity);
        User savedUser = modelMapper.map(userEntityReturn, User.class);
        // отправка в шину
        sendUserToQueue(savedUser);
        // не знаю как везде делается, но я предпочитаю не возвращать пароли на запросы по REST (мало ли)
        userEntityReturn.setPassword("*");

        return savedUser;
    }

    /**
     * Отправляем инфо по юзеру в шину
     * @param user - инфо по юзеру с фронта
     */
    public void sendUserToQueue(User user) {
        Message<User> message = MessageBuilder.withPayload(user).build();
        Message<ResponseMessage> responseMessage;
        try{
            responseMessage = messagingService.doRequest(message);
            if (responseMessage.getPayload().getMessageStatus().equals("FAIL")){
                usersErrorOnQueue.add(user);
            }
        } catch (TimeoutException e){
            throw new MasterException(ErrorCode.QUEUE_EXCEPTION);
        }
    }

    /**
     * Метод отправки юзеров повторно
     */
    @Scheduled(cron = "0 * * * * *")
    public void sendUserToQueueOnSchedule(){
        LOGGER.info("schedule job started");
        for (User user:usersErrorOnQueue){
            sendUserToQueue(user);
            usersErrorOnQueue.remove(user);
        }
    }

    /**
     * Метод для тестирования функционала
     * @param id - идентификатор юзера
     * @return - юзер
     */
    public User getUser(Long id){
        return modelMapper.map(userRepository.findById(id).get(), User.class);
    }
}
