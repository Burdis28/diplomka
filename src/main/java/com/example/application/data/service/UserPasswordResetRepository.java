package com.example.application.data.service;

import com.example.application.data.entity.UserPasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserPasswordResetRepository extends JpaRepository<UserPasswordReset, Integer> {

    @Query("SELECT r FROM UserPasswordReset r WHERE r.email=:email AND r.isValid=true AND r.validTill > :validTill")
    List<UserPasswordReset> findByEmailAndIsValid(@Param("email") String email, @Param("validTill") LocalDateTime validTill);
}
