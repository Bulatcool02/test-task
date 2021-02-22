package com.skb.testtask.controller;

import com.skb.testtask.model.User;
import com.skb.testtask.model.entity.UserEntity;
import com.skb.testtask.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
@WebMvcTest
class UserRegistrationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    private final UserEntity userEntity = new UserEntity();
    private final User user = new User();

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        user.setEmail("TEST");
        user.setLogin("TEST");
        user.setName("TEST");
        user.setPatronymic("TEST");
        user.setSurname("TEST");
        user.setPassword("msadfkjshd");

        userEntity.setEmail("TEST");
        userEntity.setLogin("TEST");
        userEntity.setName("TEST");
        userEntity.setPatronymic("TEST");
        userEntity.setSurname("TEST");
        userEntity.setPassword("msadfkjshd");
    }

    @Test
    void registerUserAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{" +
                            "    \"login\":\"aaaa\"," +
                            "    \"password\":\"test\"," +
                            "    \"email\":\"sdad\",\n" +
                            "    \"surname\":\"surname\"," +
                            "    \"name\":\"name\",\n" +
                            "    \"patronymic\":\"patronymic\"" +
                            "}")
                )
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
}