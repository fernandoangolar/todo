package ao.com.angotech.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private  static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUserDetailsService detailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException,
            IOException {

        String token = request.getHeader(JwtTokenProvider.JWT_AUTHORIZATION);

        if (token == null || !token.startsWith(JwtTokenProvider.JWT_BEARER)) {
            logger.info("JWT Token está nulo, vázio ou não iniciado com 'Bearer '");
            filterChain.doFilter(request, response);
            return;
        }

        if (!JwtTokenProvider.isTokenValid(token)) {
            logger.info("JWT TOKEN está inválido ou experirado");
            filterChain.doFilter(request, response);
            return;
        }

        String email = JwtTokenProvider.getEmailFromToken(token);

        toAuthentication(request, email);

        filterChain.doFilter(request, response);
    }

    private void toAuthentication(HttpServletRequest request, String email) {

        UserDetails userDetails = detailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(userDetails, null, userDetails.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
