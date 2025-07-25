package com.tangerineteam.gyoolworksap.security;

import com.tangerineteam.gyoolworksap.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userEntity.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getId();
    }



    //계정 만료기간
    @Override public boolean isAccountNonExpired() { return true; }
    //계정 잠금
    @Override public boolean isAccountNonLocked() { return true; }
    //자격증명(비밀번호) 만료
    @Override public boolean isCredentialsNonExpired() { return true; }
    //계정활성화
    @Override public boolean isEnabled() { return true; }

    public UserEntity getUserEntity() {
        return this.userEntity;
    }
}
