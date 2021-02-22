package com.skb.testtask.service;

import com.skb.testtask.model.ResponseMessage;
import org.springframework.messaging.Message;

/**
 * В предложенном списке интерфейсов оказался интерфейс по перехвату сообщений из очереди.
 * Честно я не понял зачем от тут нужен если у нас в классе MessagingService уже вернется ответ на наш запрос.
 * Возможно я что-то не так понял :(
 */
public class MessageListenerImpl implements MessageListener<ResponseMessage>{
    @Override
    public void handleMessage(Message<ResponseMessage> incomingMessage) {

    }
}
