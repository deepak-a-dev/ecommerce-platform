package com.icore.ecommerce_platform.serviceimpl;

import com.icore.ecommerce_platform.dao.ForgotPasswordRepository;
import com.icore.ecommerce_platform.dao.UserRepository;
import com.icore.ecommerce_platform.dto.MailBodyDto;
import com.icore.ecommerce_platform.dto.ResetPasswordDto;
import com.icore.ecommerce_platform.entity.ForgotPassword;
import com.icore.ecommerce_platform.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import com.icore.ecommerce_platform.exception.ResourceNotFoundException;
import com.icore.ecommerce_platform.exception.InvalidRequestException;

/**
 * Handles the forgot-password flow: generating a time-limited OTP, emailing it to
 * the user, and verifying it before updating the password.
 */
@Service
public class ForgotPasswordImpl {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private EmailServiceImpl emailService;
    private ForgotPasswordRepository forgotPasswordRepository;


    public ForgotPasswordImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailServiceImpl emailService, ForgotPasswordRepository forgotPasswordRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
    }

    public void sendOtpBasedOnUsername(String username) {
        User user = userRepository.usernameVerification(username);
        if (user == null) {
            throw new ResourceNotFoundException("Username '" + username + "' does not exist");
        }

        int otp = otpGenerator();
        LocalDateTime now = LocalDateTime.now();
        MailBodyDto mailBodyDto = MailBodyDto.builder()
                .to(user.getEmail())
                .text("This is the OTP for your Forgot Password request : " + otp + "\nThis OTP will be valid for the next 1 minute.")
                .subject("Forgot Password")
                .build();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .creation(now)
                .expiration(now.plusMinutes(1))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBodyDto);
        forgotPasswordRepository.save(fp);
    }

    public void verifyOtp(ResetPasswordDto resetPasswordDto) {
        LocalDateTime now = LocalDateTime.now();
        User user = userRepository.usernameVerification(resetPasswordDto.getUsername());
        ForgotPassword fp = forgotPasswordRepository.getForgotPasswordRecordByOtpAndMail(resetPasswordDto.getOtp(), user, now);

        if (fp == null) {
            throw new InvalidRequestException("OTP is incorrect or expired");
        }
        if (!Objects.equals(resetPasswordDto.getNewPassword(), resetPasswordDto.getRepeatPassword())) {
            throw new InvalidRequestException("Passwords do not match");
        }

        String encodedPassword = passwordEncoder.encode(resetPasswordDto.getNewPassword());
        userRepository.updatePassword(encodedPassword, user.getUserId());
    }


    private int otpGenerator() {
        int otpLength = 6;
        int otp = 0;
        Random random = new Random();
        for (int i = 0; i < otpLength; i++) {
            int digit = random.nextInt(10);
            otp = otp * 10 + digit;
        }
        return otp;
    }
}
