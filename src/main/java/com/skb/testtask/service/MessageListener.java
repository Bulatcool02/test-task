package com.skb.testtask.service;


import org.springframework.messaging.Message;

public interface MessageListener<T> {
    void handleMessage(Message<T> incomingMessage);
}
