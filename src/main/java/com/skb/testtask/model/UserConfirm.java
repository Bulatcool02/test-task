package com.skb.testtask.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserConfirm {
    private Long userId;
    private UUID requestMessageId;
    private String confirmStatus;
}
