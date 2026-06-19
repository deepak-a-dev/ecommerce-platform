package com.icore.ecommerce_platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResetPasswordDto {

    @NotBlank(message = "Username is required")
    private String username;

    private int otp;

    @NotBlank(message = "New password is required")
    @Size(min = 6, max = 72, message = "Password must be between 6 and 72 characters")
    private String newPassword;

    @NotBlank(message = "Repeat password is required")
    private String repeatPassword;
}
