package com.tangerineteam.gyoolworksap.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class UserEntity {
    @Id
    private String id;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String email;
}
