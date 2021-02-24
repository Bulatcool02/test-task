package com.skb.testtask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * ДТО подтверждения запроса
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {
    private UUID messageId;
    private String messageStatus;
}
