package com.tangerineteam.gyoolworksap.service;

import com.tangerineteam.gyoolworksap.dto.JwtToken;
import com.tangerineteam.gyoolworksap.dto.UserInfo;
import com.tangerineteam.gyoolworksap.entity.UserEntity;
import com.tangerineteam.gyoolworksap.repository.UserRepository;
import com.tangerineteam.gyoolworksap.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManagerBuilder  authenticationManagerBuilder;

    @Autowired
    private JwtProvider jwtProvider;

    public boolean isExistUserId(UserInfo user){

        boolean isExist = userRepository.existsById(user.getId());

        return isExist;
    }



    public boolean addUser(UserInfo user) {
        //아이디 중복체크
        boolean isExists = isExistUserId(user);
        if(isExists){
            return false;
        }

        List<String> defaultRoles = new ArrayList<>();
        defaultRoles.add("USER");


        UserEntity userEntity = UserEntity.builder()
                .id(user.getId())
                .password(user.getPassword())
                .name(user.getName())
                .email(user.getEmail())
                .roles(defaultRoles)
                .build();
        userRepository.save(userEntity);
        isExists = userRepository.existsById(userEntity.getId());

        return isExists;
    }



    @Transactional
    public JwtToken login(UserInfo user) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword());

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            JwtToken jwtToken = jwtProvider.generateToken(authentication);
            return jwtToken;

        } catch (BadCredentialsException e) {
            //아이디는 존재 하지만 비밀번호가 틀린경우
            throw e;
        } catch (UsernameNotFoundException e) {
            //아이디가 db에 존재하지 않는경우
            throw e;
        } catch (DisabledException e) {
            //계정이 비활성화 된 경우
            throw e;
        } catch (LockedException e) {
            //계정이 잠긴경우
            throw e;
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }
}
