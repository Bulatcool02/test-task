package com.skb.testtask.service;

import java.util.concurrent.TimeoutException;

public interface SendMailer {
    void sendMail (String fromAddress, String toAddress, String messageBody) throws TimeoutException;
}
