package com.icore.ecommerce_platform.config;

import com.icore.ecommerce_platform.dao.TokenRepository;
import com.icore.ecommerce_platform.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * On logout, marks the caller's JWT as logged-out in the token store so it can no
 * longer be used for authentication, even though it has not yet expired.
 */
@Component
public class CustomLogoutHandler implements LogoutHandler {


    private TokenRepository tokenRepository;

    public CustomLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        String token = authHeader.substring(7);

        // get stored token from database
        Token storedToken = tokenRepository.findByToken(token).orElse(null);
        // invalidate the token i.e make logout true
        // save the token
        if (token != null) {
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
        }


    }
}
