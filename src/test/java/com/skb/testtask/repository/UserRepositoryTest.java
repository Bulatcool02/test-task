package com.skb.testtask.repository;

import com.skb.testtask.model.User;
import com.skb.testtask.model.entity.UserEntity;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByEmail() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("TEST");
        userEntity.setLogin("TEST");
        userEntity.setName("TEST");
        userEntity.setPatronymic("TEST");
        userEntity.setSurname("TEST");
        userEntity.setPassword("msadfkjshd");
        Long id = entityManager.persistAndGetId(userEntity, Long.class);
        assertNotNull(id);
        UserEntity userEntityResult = userRepository.findByEmail("TEST");
        assertNotNull(userEntity);
        assertEquals(userEntityResult.getEmail(), userEntity.getEmail());
    }

    @Test
    void findByLogin() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("TEST");
        userEntity.setLogin("TEST");
        userEntity.setName("TEST");
        userEntity.setPatronymic("TEST");
        userEntity.setSurname("TEST");
        userEntity.setPassword("msadfkjshd");

        Long id = entityManager.persistAndGetId(userEntity, Long.class);
        assertNotNull(id);
        UserEntity userEntityResult = userRepository.findByLogin("TEST");
        assertNotNull(userEntity);
        assertEquals(userEntityResult.getLogin(), userEntity.getLogin());
    }
}