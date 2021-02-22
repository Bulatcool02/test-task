package com.skb.testtask.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ДТО для передачи данных о юзере по REST или в шину событий.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
    private String email;

    private String surname;

    private String name;

    private String patronymic;

    private UserStatus userStatus;
}
