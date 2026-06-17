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

    public String sendOtp(String email) {
        User user = userRepository.getUserByMail(email);
        if (user != null) {
            int otp = otpGenerator();
            LocalDateTime now = LocalDateTime.now();

            MailBodyDto mailBodyDto = MailBodyDto.builder()
                    .to(email)
                    .text("This is the OTP for your Forgot Password request : " + otp + "\nThis OTP will be valid for the next 1 minute.")
                    .subject("Forgot Password")
                    .build();

            ForgotPassword fp = ForgotPassword.builder()
                    .otp(otp)
                    // .expiration(new Date(System.currentTimeMillis() + 90 * 1000))
                    .creation(now)
                    .expiration(now.plusMinutes(1))
                    .user(user)
                    .build();

            emailService.sendSimpleMessage(mailBodyDto);
            forgotPasswordRepository.save(fp);

            return "Otp sent to your email";
        } else {
            return "Email does not exist in the database.";
        }
    }

    public String sendOtpBasedOnUsername(String username) {
        User user = userRepository.usernameVerification(username);
        if (user != null) {
            int otp = otpGenerator();
            LocalDateTime now = LocalDateTime.now();
            MailBodyDto mailBodyDto = MailBodyDto.builder()
                    .to(user.getEmail())
                    .text("This is the OTP for your Forgot Password request : " + otp + "\nThis OTP will be valid for the next 1 minute.")
                    .subject("Forgot Password")
                    .build();

            ForgotPassword fp = ForgotPassword.builder()
                    .otp(otp)
                    // .expiration(new Date(System.currentTimeMillis() + 90 * 1000))
                    .creation(now)
                    .expiration(now.plusMinutes(1))
                    .user(user)
                    .build();


            emailService.sendSimpleMessage(mailBodyDto);
            forgotPasswordRepository.save(fp);

            return "Otp sent to your email";
        } else {
            return "Username does not exist in the database";
        }
    }

    public String verifyOtp(ResetPasswordDto resetPasswordDto) {
        LocalDateTime now = LocalDateTime.now();
        //User user = userRepository.getUserByMail(resetPasswordDto.getEmail());
        User user = userRepository.usernameVerification(resetPasswordDto.getUsername());
        ForgotPassword fp = forgotPasswordRepository.getForgotPasswordRecordByOtpAndMail(resetPasswordDto.getOtp(), user, now);

        if (fp == null) {
            return "OTP is incorrect / expired.";
        } else {
            if (Objects.equals(resetPasswordDto.getNewPassword(), resetPasswordDto.getRepeatPassword())) {
                String encodedPassword = passwordEncoder.encode(resetPasswordDto.getNewPassword());
                userRepository.updatePassword(encodedPassword, user.getUserId());
                return "Password Updated Successfully";
            } else {
                return "Please enter a matching password";
            }
        }
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
