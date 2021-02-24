package com.skb.testtask.service;

import org.springframework.messaging.Message;

public interface MessageListener<UserConfirm> {
    void handleMessage(Message<com.skb.testtask.model.UserConfirm> incomingMessage);
}
