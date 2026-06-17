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

import java.time.LocalDateTime;
import java.util.List;

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
    public String userRegister(RegistrationFormDto registrationFormDto) {

        User check = userRepository.usernameVerification(registrationFormDto.getUsername());
        if (check != null) {
            return "USERNAME ALREADY EXISTS. PLEASE CHOOSE A DIFFERENT USERNAME.";
        } else {
            User user = new User();
            user.setFirstName(registrationFormDto.getFirstName());
            user.setLastName(registrationFormDto.getLastName());
            user.setPhoneNumber(registrationFormDto.getPhoneNumber());
            user.setEmail(registrationFormDto.getEmail());
            user.setUsername(registrationFormDto.getUsername());
            user.setPassword(passwordEncoder.encode(registrationFormDto.getPassword()));
            user.setRole(Role.USER);
            userRepository.save(user);
            return "REGISTRATION SUCCESSFUL";
        }

    }



    @Override
    public String userLogin(LoginFormDto loginFormDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginFormDto.getUsername(),
                        loginFormDto.getPassword()
                )
        );

        User user = userRepository.findByUsername(loginFormDto.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);

        // before saving query the data
        List<Token> validTokenListByUser = tokenRepository.findAllTokenByUser(user.getUserId());
        if (!validTokenListByUser.isEmpty()) {
            validTokenListByUser.forEach(t-> {
                t.setLoggedOut(true);
            });
        }
        tokenRepository.saveAll(validTokenListByUser);

        // save generated token
        Token token1 = new Token();
        token1.setToken(token);
        token1.setLoggedOut(false);
        token1.setCreation(LocalDateTime.now());
        token1.setExpiration(LocalDateTime.now().plusMinutes(10));
        token1.setUser(user);
        tokenRepository.save(token1);


        return "Login Success.\n\nYour token = " + token;
    }




//        User user = userRepository.usernameVerification(loginFormDto.getUsername());
//        if (user != null) {
//            String password = loginFormDto.getPassword();
//            String encodedPassword = user.getPassword();
//            Boolean isPasswordCorrect = passwordEncoder.matches(password, encodedPassword);
//            if (isPasswordCorrect) {
//                Optional<User> u = userRepository.usernamePasswordMatches(loginFormDto.getUsername(), user.getPassword());
//                if (u.isPresent()) {
//                    String token = jwtService.generateToken(user);
//                    return "Login Success\n\nYour token = "+token;
//                } else {
//                    return "Login Failed";
//                }
//            } else {
//                return "Incorrect Password. Try Again";
//            }
//        } else {
//            return "Incorrect Username. Try Again";
//        }
//    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
