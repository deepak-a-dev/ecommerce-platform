package com.icore.ecommerce_platform.config;

import com.icore.ecommerce_platform.dao.UserRepository;
import com.icore.ecommerce_platform.entity.Role;
import com.icore.ecommerce_platform.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds an initial ADMIN account on startup if one does not already exist,
 * so the application is usable without manually editing the database.
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminEmail;
    private final String adminPassword;

    public AdminInitializer(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            @Value("${app.admin.username}") String adminUsername,
                            @Value("${app.admin.email}") String adminEmail,
                            @Value("${app.admin.password}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (userRepository.usernameVerification(adminUsername) != null) {
            return; // admin already exists — nothing to do
        }

        User admin = new User();
        admin.setFirstName("System");
        admin.setLastName("Admin");
        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPhoneNumber("");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        log.info("Seeded initial ADMIN account: {}", adminUsername);
    }
}