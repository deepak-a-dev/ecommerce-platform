package com.icore.ecommerce_platform.dao;


import com.icore.ecommerce_platform.entity.ForgotPassword;
import com.icore.ecommerce_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.otp = :otp AND fp.user = :user AND fp.expiration > :now ORDER BY fp.expiration DESC LIMIT 1")
    ForgotPassword getForgotPasswordRecordByOtpAndMail(int otp, User user, LocalDateTime now);
}
