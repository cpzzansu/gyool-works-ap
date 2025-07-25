package com.tangerineteam.gyoolworksap.service;

import com.tangerineteam.gyoolworksap.dto.CompanyInfo;
import com.tangerineteam.gyoolworksap.dto.JwtToken;
import com.tangerineteam.gyoolworksap.dto.UserInfo;
import com.tangerineteam.gyoolworksap.entity.CompanyEntity;
import com.tangerineteam.gyoolworksap.entity.UserEntity;
import com.tangerineteam.gyoolworksap.repository.CompanyRepository;
import com.tangerineteam.gyoolworksap.repository.UserRepository;
import com.tangerineteam.gyoolworksap.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AuthenticationManagerBuilder  authenticationManagerBuilder;

    @Autowired
    private JwtProvider jwtProvider;


    public CompanyEntity findCompany(CompanyInfo company){
        return companyRepository.findByBusinessNum(company.getBusinessNum())
                .orElse(null);
    }
}
