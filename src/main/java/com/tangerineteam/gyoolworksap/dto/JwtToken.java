package com.tangerineteam.gyoolworksap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
    //인증타입
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
