package com.skb.testtask.service;

import com.skb.testtask.model.ResponseMessage;
import com.skb.testtask.model.User;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final SendEmailImpl sendEmailImpl;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, PasswordEncoder encoder, MessagingServiceImpl messagingService, SendEmailImpl sendEmailImpl, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.messagingService = messagingService;
        this.sendEmailImpl = sendEmailImpl;
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
        // отправка в шину
        sendUserToQueue(user);
        // не знаю как везде делается, но я предпочитаю не возвращать пароли на запросы по REST (мало ли)
        userEntityReturn.setPassword("*");
        return modelMapper.map(userEntityReturn, User.class);
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
        } catch (TimeoutException e){
            throw new MasterException(ErrorCode.QUEUE_EXCEPTION);
        }
        // после того как дождались ответа чекаем,что нам отправить на почту юзеру - письмо с подтверждением/отказом на регистрацию
        if (responseMessage != null){
            UserEntity userEntity = this.userRepository.findByEmail(user.getEmail());
            if (userEntity == null){
                throw new MasterException(ErrorCode.USER_NOT_FOUND);
            }
            ResponseMessage response = responseMessage.getPayload();
            String content;
            // в зависимости от ответа меняем статус для юзера на соответсвующий и рассылаем письма с необходиммым контентом
            if (response.getMessageStatus().equals("OK")){
                userEntity.setStatus(UserStatus.ACTIVE);
                userRepository.save(userEntity);
                content = "Good email";
                LOGGER.info("Message was send with status OK");
            } else {
                userEntity.setStatus(UserStatus.INACTIVE);
                userRepository.save(userEntity);
                content = "Bad email";
                LOGGER.info("Message was send with status FAIL");
            }
            sendEmail(user.getEmail(), content);
        }
    }

    public void sendEmail(String email, String text) {
        try{
            sendEmailImpl.sendMail("TEST@MAIL.RU", email, text);
        } catch (TimeoutException e){
            throw new MasterException(ErrorCode.EMAIL_EXCEPTION);
        }
    }
}
