package com.tangerineteam.gyoolworksap.repository;

import com.tangerineteam.gyoolworksap.entity.CompanyEntity;
import com.tangerineteam.gyoolworksap.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {

    Optional<CompanyEntity> findByBusinessNum(String businessNum);
}
