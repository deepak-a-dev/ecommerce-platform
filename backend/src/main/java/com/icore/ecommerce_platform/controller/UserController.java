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
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.icore.ecommerce_platform.dto.UserPublicAccessDto;
import com.icore.ecommerce_platform.dto.AuthResponseDto;
import jakarta.validation.Valid;

import java.util.List;
import com.icore.ecommerce_platform.dto.MessageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * REST endpoints for user-facing operations under {@code /api/user}:
 * registration, login, password reset (OTP by email), and public product browsing.
 */
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
    public ResponseEntity<UserPublicAccessDto> userRegister(@Valid @RequestBody RegistrationFormDto registrationFormDto) {
        UserPublicAccessDto created = userService.userRegister(registrationFormDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> userLogin(@Valid @RequestBody LoginFormDto loginFormDto) {
        AuthResponseDto response = userService.userLogin(loginFormDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<MessageResponseDto> forgotPassword(@RequestParam String username) {
        forgotPassword.sendOtpBasedOnUsername(username);
        return ResponseEntity.ok(new MessageResponseDto("OTP sent to your email"));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<MessageResponseDto> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        forgotPassword.verifyOtp(resetPasswordDto);
        return ResponseEntity.ok(new MessageResponseDto("Password updated successfully"));
    }

    @GetMapping("/browse/category")
    public ResponseEntity<List<String>> listCategory() {
        return ResponseEntity.ok(productRepository.getCategoryList());
    }

    @GetMapping("browse/category/{name}")
    public ResponseEntity<List<Product>> basedOnCategory(@PathVariable("name") String name) {
        return ResponseEntity.ok(productRepository.basedOnCategory(name));
    }

    @GetMapping("/browse/listProduct")
    public ResponseEntity<List<Product>> listProduct() {
        return ResponseEntity.ok(productRepository.getAllProducts());
    }

    @GetMapping("/browse/products")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(productRepository.searchProducts(category, search, pageable));
    }

}
