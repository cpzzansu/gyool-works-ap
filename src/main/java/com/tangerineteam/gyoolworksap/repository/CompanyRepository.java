package com.tangerineteam.gyoolworksap.repository;

import com.tangerineteam.gyoolworksap.entity.CompanyEntity;
import com.tangerineteam.gyoolworksap.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {

    Optional<CompanyEntity> findByBusinessNum(String businessNum);
}
