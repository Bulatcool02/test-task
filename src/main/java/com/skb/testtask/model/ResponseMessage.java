package com.skb.testtask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * ДТО ответа на запрос по регистрации юзера
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {
    private UUID messageId;
    private String messageStatus;
}
