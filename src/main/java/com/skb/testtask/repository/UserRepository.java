package com.skb.testtask.repository;

import com.skb.testtask.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(@Param("email") String email);
    UserEntity findByLogin(@Param("login") String login);
}
