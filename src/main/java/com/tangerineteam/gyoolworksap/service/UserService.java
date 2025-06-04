package com.tangerineteam.gyoolworksap.service;

import com.tangerineteam.gyoolworksap.dto.UserInfo;
import com.tangerineteam.gyoolworksap.entity.UserEntity;
import com.tangerineteam.gyoolworksap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean addUser(UserInfo user) {
        boolean isExists = false;

        //아이디 중복체크
        isExists = userRepository.existsById(user.getId());

        if (isExists) {
            return false;
        }

        UserEntity userEntity = UserEntity.builder()
                .id(user.getId())
                .password(user.getPassword())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        userRepository.save(userEntity);
        isExists = userRepository.existsById(userEntity.getId());
        return isExists;
    }
}
