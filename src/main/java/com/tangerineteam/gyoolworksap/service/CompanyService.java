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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AuthenticationManagerBuilder  authenticationManagerBuilder;

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${api.key}")
    private String serviceKey;



    public CompanyEntity findCompany(CompanyInfo company){
        return companyRepository.findByBusinessNum(company.getBusinessNum())
                .orElse(null);
    }

    public Map<String, Object> checkBusinessStatus(CompanyInfo company) {
        String businessNum = company.getBusinessNum();
        String ceoName = company.getCeoName();
        String start_dt = String.valueOf(company.getOpenDate());

        Map<String, Object> biz = new HashMap<>();
        biz.put("b_no", businessNum);
        biz.put("start_dt", start_dt);
        biz.put("p_nm", ceoName);
        biz.put("p_nm2", "");
        biz.put("b_nm", "");
        biz.put("corp_no", "");
        biz.put("b_sector", "");
        biz.put("b_type", "");
        biz.put("b_adr", "");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("businesses", List.of(biz));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            String encodedKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
            String fullUrl = "https://api.odcloud.kr/api/nts-businessman/v1/validate?serviceKey=" + encodedKey;
            URI uri = URI.create(fullUrl);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.postForEntity(uri, entity, Map.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("사업자 상태 조회 API 호출 실패: " + e.getMessage(), e);
        }
    }

    public void addCompany(CompanyInfo company) {
        CompanyEntity entity = CompanyEntity.builder()
                .businessNum(company.getBusinessNum())
                .ceoName(company.getCeoName())
                //.companyName(company.getCompanyName())
                .openDate(company.getOpenDate())
                .registerDate(new Date())
                .build();

        companyRepository.save(entity);
    }
}
