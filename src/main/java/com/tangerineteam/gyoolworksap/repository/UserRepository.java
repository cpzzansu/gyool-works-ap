package com.tangerineteam.gyoolworksap.repository;

import com.tangerineteam.gyoolworksap.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, String> {
    //아이디 중복확인
    boolean existsById(String id);

    Optional<UserEntity> findById(String id);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.companyId = :companyId, u.approvalStatus = :approvalStatus WHERE u.id = :userId")
    int updateCompanyAndApprovalStatus(@Param("userId") String userId,
                                       @Param("companyId") Long companyId,
                                       @Param("approvalStatus") String approvalStatus);
}
