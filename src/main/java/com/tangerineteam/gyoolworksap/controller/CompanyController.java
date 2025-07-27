package com.tangerineteam.gyoolworksap.controller;


import com.tangerineteam.gyoolworksap.dto.CompanyInfo;
import com.tangerineteam.gyoolworksap.dto.UserInfo;
import com.tangerineteam.gyoolworksap.entity.CompanyEntity;
import com.tangerineteam.gyoolworksap.repository.UserRepository;
import com.tangerineteam.gyoolworksap.service.CompanyService;
import com.tangerineteam.gyoolworksap.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserRepository userRepository;

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

    @PostMapping("/createCompany")
    public ResponseEntity<?> createCompany(@RequestBody CompanyInfo company) {
        Map<String, Object> result = companyService.checkBusinessStatus(company);
        System.out.println(result);
        List<Map<String, Object>> dataList = (List<Map<String, Object>>) result.get("data");

        if (dataList != null && !dataList.isEmpty()) {
            Map<String, Object> dataItem = dataList.get(0);  // 첫 번째 결과만 사용
            Map<String, Object> status = (Map<String, Object>) dataItem.get("status");

            if (status != null) {
                String bSttCd = (String) status.get("b_stt_cd");
                if ("01".equals(bSttCd)) {
                    // 유효한 계속사업자일 경우
                    System.out.println("계속사업자입니다.");
                    companyService.addCompany(company);
                    return ResponseEntity.ok("등록되었습니다.");
                } else {
                    // 유효하지 않음
                    System.out.println("유효하지 않은 사업자 상태입니다: " + bSttCd);
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("유효하지 않은 사업자 상태입니다.");
                }
            } else {
                System.out.println("status 항목이 없습니다.");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("status 항목이 없습니다.");
            }
        } else {
            System.out.println("data 항목이 비어 있습니다.");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("조회된 사업자 정보가 없습니다.");
        }
    }

    @PostMapping("/requestJoin")
    public ResponseEntity<?> requestJoin(@RequestBody CompanyInfo company) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getName();  // 여기서 userId = JWT의 subject
        System.out.println(userId);

        userRepository.updateCompanyAndApprovalStatus(userId, company.getCompanyId(), "N");

        return ResponseEntity.ok("가입 요청 완료");
    }
}
