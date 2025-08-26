package ao.com.angotech.service.impl;

import ao.com.angotech.jwt.JwtToken;
import ao.com.angotech.jwt.JwtTokenProvider;
import ao.com.angotech.service.AuthService;
import ao.com.angotech.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserService userService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public JwtToken authenticate(String email, String password) {

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));

            if (authentication.isAuthenticated()) {
                return JwtTokenProvider.createToken(email);
            }

            throw new BadCredentialsException("Credenciais inválidas");
        } catch (Exception e) {
            throw new BadCredentialsException("Email ou senha incorretos");
        }
    }
}
