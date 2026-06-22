package com.icore.ecommerce_platform.serviceimpl;

import com.icore.ecommerce_platform.dao.TokenRepository;
import com.icore.ecommerce_platform.dao.UserRepository;
import com.icore.ecommerce_platform.dto.LoginFormDto;
import com.icore.ecommerce_platform.dto.RegistrationFormDto;
import com.icore.ecommerce_platform.entity.Role;
import com.icore.ecommerce_platform.entity.Token;
import com.icore.ecommerce_platform.entity.User;
import com.icore.ecommerce_platform.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.icore.ecommerce_platform.exception.DuplicateResourceException;
import com.icore.ecommerce_platform.dto.UserPublicAccessDto;
import com.icore.ecommerce_platform.dto.AuthResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import com.icore.ecommerce_platform.dto.RefreshTokenRequestDto;
import com.icore.ecommerce_platform.exception.InvalidRequestException;
import com.icore.ecommerce_platform.exception.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;

/**
 * Default implementation of {@link UserService}.
 */
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtServiceImpl jwtService;
    private AuthenticationManager authenticationManager;
    private TokenRepository tokenRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtServiceImpl jwtService, AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public UserPublicAccessDto userRegister(RegistrationFormDto registrationFormDto) {
        User existing = userRepository.usernameVerification(registrationFormDto.getUsername());
        if (existing != null) {
            throw new DuplicateResourceException(
                    "Username '" + registrationFormDto.getUsername() + "' is already taken");
        }

        User user = new User();
        user.setFirstName(registrationFormDto.getFirstName());
        user.setLastName(registrationFormDto.getLastName());
        user.setPhoneNumber(registrationFormDto.getPhoneNumber());
        user.setEmail(registrationFormDto.getEmail());
        user.setUsername(registrationFormDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationFormDto.getPassword()));
        user.setRole(Role.USER);

        User saved = userRepository.save(user);

        return new UserPublicAccessDto(
                saved.getUserId(), saved.getFirstName(), saved.getLastName(),
                saved.getPhoneNumber(), saved.getEmail(), saved.getUsername());
    }


    @Override
    public AuthResponseDto userLogin(LoginFormDto loginFormDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginFormDto.getUsername(),
                        loginFormDto.getPassword()
                )
        );

        User user = userRepository.findByUsername(loginFormDto.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // before saving query the data
        List<Token> validTokenListByUser = tokenRepository.findAllTokenByUser(user.getUserId());
        if (!validTokenListByUser.isEmpty()) {
            validTokenListByUser.forEach(t -> {
                t.setLoggedOut(true);
            });
        }
        tokenRepository.saveAll(validTokenListByUser);

        // save generated token
        Token token1 = new Token();
        token1.setToken(token);
        token1.setLoggedOut(false);
        token1.setCreation(LocalDateTime.now());
        token1.setExpiration(LocalDateTime.now().plusMinutes(15));
        token1.setUser(user);
        tokenRepository.save(token1);


        return new AuthResponseDto(token, refreshToken);
    }

    @Override
    public List<UserPublicAccessDto> getAllUsers() {
        return userRepository.getAllUsers().stream()
                .map(u -> new UserPublicAccessDto(
                        u.getUserId(), u.getFirstName(), u.getLastName(),
                        u.getPhoneNumber(), u.getEmail(), u.getUsername()))
                .toList();
    }

    @Override
    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        String refreshToken = request.refreshToken();

        String username;
        try {
            username = jwtService.extractUsername(refreshToken);   // throws if invalid/expired
        } catch (JwtException ex) {
            throw new InvalidRequestException("Invalid or expired refresh token");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String newAccessToken = jwtService.generateToken(user);

        // store the new access token so the JWT filter's DB check accepts it
        Token tokenEntity = new Token();
        tokenEntity.setToken(newAccessToken);
        tokenEntity.setLoggedOut(false);
        tokenEntity.setCreation(LocalDateTime.now());
        tokenEntity.setExpiration(LocalDateTime.now().plusMinutes(15));
        tokenEntity.setUser(user);
        tokenRepository.save(tokenEntity);

        return new AuthResponseDto(newAccessToken, refreshToken);
    }
}
