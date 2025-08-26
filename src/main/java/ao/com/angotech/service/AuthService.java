package ao.com.angotech.service;

import ao.com.angotech.jwt.JwtToken;

public interface AuthService {
    JwtToken authenticate(String email, String password);
}
