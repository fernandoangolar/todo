package ao.com.angotech.controller.user;

import ao.com.angotech.dto.user.UserAuth;
import ao.com.angotech.jwt.JwtToken;
import ao.com.angotech.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authservice;

    public AuthController(AuthService authservice) {
        this.authservice = authservice;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> authenticate(@Valid @RequestBody UserAuth userAuth) {
        JwtToken token = authservice.authenticate(userAuth.email(), userAuth.password());
        return ResponseEntity.status(HttpStatus.OK)
                .body(token);
    }
}
