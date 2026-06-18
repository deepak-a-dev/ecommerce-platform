package com.icore.ecommerce_platform.service;

import com.icore.ecommerce_platform.dto.LoginFormDto;
import com.icore.ecommerce_platform.dto.RegistrationFormDto;
import com.icore.ecommerce_platform.entity.User;
import com.icore.ecommerce_platform.dto.UserPublicAccessDto;

import java.util.List;

/**
 * Business operations for user accounts: registration, login (JWT issuance),
 * and admin user listing.
 */
public interface UserService {

    UserPublicAccessDto userRegister(RegistrationFormDto registrationFormDto);

    String userLogin(LoginFormDto loginFormDto);

    List<User> getAllUsers();
}
