package com.icore.ecommerce_platform.service;

import com.icore.ecommerce_platform.dto.LoginFormDto;
import com.icore.ecommerce_platform.dto.RegistrationFormDto;
import com.icore.ecommerce_platform.entity.User;

import java.util.List;

public interface UserService {

    String userRegister(RegistrationFormDto registrationFormDto);

    String userLogin(LoginFormDto loginFormDto);

    List<User> getAllUsers();
}
