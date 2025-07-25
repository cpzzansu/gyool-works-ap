package com.tangerineteam.gyoolworksap.controller;


import com.tangerineteam.gyoolworksap.dto.CompanyInfo;
import com.tangerineteam.gyoolworksap.dto.UserInfo;
import com.tangerineteam.gyoolworksap.entity.CompanyEntity;
import com.tangerineteam.gyoolworksap.service.CompanyService;
import com.tangerineteam.gyoolworksap.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;


    //회사검색 param: businessNum
    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody CompanyInfo company) {
        CompanyEntity companyEntity = companyService.findCompany(company);

        if (companyEntity == null) return ResponseEntity.ok(null);

        CompanyInfo companyInfo = CompanyInfo.builder()
                .companyId(companyEntity.getCompanyId())
                .companyName(companyEntity.getCompanyName())
                .ceoName(companyEntity.getCeoName())
                .build();
        return ResponseEntity.ok(companyInfo);
    }

}
