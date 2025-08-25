package ao.com.angotech.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    // Vai retornar 401 - quando o user tenta acessar um recuso quando não tiver altenticado...
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException,
            ServletException {

        logger.info("Http Status 401 {}", authException.getMessage());

        response.setHeader("www-authenticate", "Bearer realm='/api/v1/auth'");
        response.sendError(401);
    }
}
