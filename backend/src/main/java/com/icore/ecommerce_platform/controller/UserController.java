package com.icore.ecommerce_platform.controller;

import com.icore.ecommerce_platform.dao.ProductRepository;
import com.icore.ecommerce_platform.dto.LoginFormDto;
import com.icore.ecommerce_platform.dto.RegistrationFormDto;
import com.icore.ecommerce_platform.dto.ResetPasswordDto;
import com.icore.ecommerce_platform.entity.Product;
import com.icore.ecommerce_platform.service.UserService;
import com.icore.ecommerce_platform.serviceimpl.ForgotPasswordImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    private UserService userService;
    private ForgotPasswordImpl forgotPassword;
    private ProductRepository productRepository;

    @Autowired
    public UserController(UserService userService, ForgotPasswordImpl forgotPassword, ProductRepository productRepository) {
        this.userService = userService;
        this.forgotPassword = forgotPassword;
        this.productRepository = productRepository;
    }

    @PostMapping("/register")
    public String userRegister(@RequestBody RegistrationFormDto registrationFormDto) {
        return userService.userRegister(registrationFormDto);
    }

    @PostMapping("/login")
    public String userLogin(@RequestBody LoginFormDto loginFormDto) {
        return userService.userLogin(loginFormDto);
    }

    @PostMapping("/forgotPassword")
    public String demo(@RequestParam String username) {
        return forgotPassword.sendOtpBasedOnUsername(username);
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return forgotPassword.verifyOtp(resetPasswordDto);
    }

    @GetMapping("/browse/category")
    public List<String> listCategory() {
        return productRepository.getCategoryList();
    }

    @GetMapping("browse/category/{name}")
    public List<Product> basedOnCategory(@PathVariable("name") String name) {
        return productRepository.basedOnCategory(name);
    }

    @GetMapping("/browse/listProduct")
    public List<Product> listProduct() {
        return productRepository.getAllProducts();
    }

}
