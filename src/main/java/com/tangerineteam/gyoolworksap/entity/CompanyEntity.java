package com.tangerineteam.gyoolworksap.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "company")
public class CompanyEntity {
    @Id
    private String companyId;

    @Column
    private String businessNum;

    @Column
    private String ceoName;

    @Column
    private String companyName;

    @Column
    private Date openDate;

    @Column
    private Date registerDate;

}
