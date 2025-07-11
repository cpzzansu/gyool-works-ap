package com.tangerineteam.gyoolworksap.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
public class UserInfo {

    private String id;
    private String name;
    private String password;
    private String email;
    private String role;
    private String confirmNum;
}
