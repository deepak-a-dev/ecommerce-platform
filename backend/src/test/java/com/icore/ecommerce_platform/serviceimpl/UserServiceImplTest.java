package com.icore.ecommerce_platform.serviceimpl;

import com.icore.ecommerce_platform.dao.TokenRepository;
import com.icore.ecommerce_platform.dao.UserRepository;
import com.icore.ecommerce_platform.dto.RegistrationFormDto;
import com.icore.ecommerce_platform.dto.UserPublicAccessDto;
import com.icore.ecommerce_platform.entity.Role;
import com.icore.ecommerce_platform.entity.User;
import com.icore.ecommerce_platform.exception.DuplicateResourceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtServiceImpl jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private TokenRepository tokenRepository;

    @InjectMocks private UserServiceImpl userService;

    @Test
    void userRegister_throwsWhenUsernameAlreadyTaken() {
        // Arrange
        RegistrationFormDto dto = new RegistrationFormDto();
        dto.setUsername("ada_l");
        when(userRepository.usernameVerification("ada_l")).thenReturn(new User());

        // Act + Assert
        assertThatThrownBy(() -> userService.userRegister(dto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("ada_l");

        verify(userRepository, never()).save(any());
    }

    @Test
    void userRegister_savesEncodedPassword_andReturnsDto() {
        // Arrange
        RegistrationFormDto dto = new RegistrationFormDto();
        dto.setFirstName("Ada");
        dto.setLastName("Lovelace");
        dto.setPhoneNumber("9999999999");
        dto.setEmail("ada@example.com");
        dto.setUsername("ada_l");
        dto.setPassword("secret123");

        when(userRepository.usernameVerification("ada_l")).thenReturn(null);
        when(passwordEncoder.encode("secret123")).thenReturn("ENCODED");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setUserId(1);
            return u;
        });

        // Act
        UserPublicAccessDto result = userService.userRegister(dto);

        // Assert — the returned DTO
        assertThat(result.getUserId()).isEqualTo(1);
        assertThat(result.getUsername()).isEqualTo("ada_l");
        assertThat(result.getEmail()).isEqualTo("ada@example.com");

        // Assert — what got persisted (password encoded, role USER)
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertThat(saved.getPassword()).isEqualTo("ENCODED");
        assertThat(saved.getRole()).isEqualTo(Role.USER);
    }
}