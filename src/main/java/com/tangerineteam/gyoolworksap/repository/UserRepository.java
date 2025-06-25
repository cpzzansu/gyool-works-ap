package com.tangerineteam.gyoolworksap.repository;

import com.tangerineteam.gyoolworksap.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, String> {
    //아이디 중복확인
    boolean existsById(String id);

    Optional<UserEntity> findById(String id);
}
