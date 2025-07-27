package com.tangerineteam.gyoolworksap.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@Builder
public class CompanyInfo {

    private Long companyId;
    private String businessNum;
    private String ceoName;
    private String companyName;
    private String openDate;
    private Date registerDate;

}
