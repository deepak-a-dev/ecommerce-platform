package com.icore.ecommerce_platform.service;

import com.icore.ecommerce_platform.dto.LoginFormDto;
import com.icore.ecommerce_platform.dto.RegistrationFormDto;
import com.icore.ecommerce_platform.entity.User;
import com.icore.ecommerce_platform.dto.UserPublicAccessDto;
import com.icore.ecommerce_platform.dto.AuthResponseDto;

import java.util.List;
import com.icore.ecommerce_platform.dto.RefreshTokenRequestDto;

/**
 * Business operations for user accounts: registration, login (JWT issuance),
 * and admin user listing.
 */
public interface UserService {

    UserPublicAccessDto userRegister(RegistrationFormDto registrationFormDto);

    AuthResponseDto userLogin(LoginFormDto loginFormDto);

    List<UserPublicAccessDto> getAllUsers();

    AuthResponseDto refreshToken(RefreshTokenRequestDto request);
}
