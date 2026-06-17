package com.icore.ecommerce_platform.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResetPasswordDto {
    private String username;
    private int otp;
    private String newPassword;
    private String repeatPassword;
}
