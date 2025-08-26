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
import org.springframework.util.StringUtils;
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

        String token = extractTokenFromRequest(request);

        if (!StringUtils.hasText(token)) {
            logger.debug("Token não encontrado no cabeçalho Authorization");
            filterChain.doFilter(request, response);
            return;
        }

        if (!JwtTokenProvider.isTokenValid(token)) {
            logger.warn("Token JWT inválido ou expirado");
            filterChain.doFilter(request, response);
            return;
        }

        String email = JwtTokenProvider.getEmailFromToken(token);

        if (StringUtils.hasText(email) && SecurityContextHolder.getContext().getAuthentication() == null) {
            toAuthentication(request, email);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(JwtTokenProvider.JWT_AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(JwtTokenProvider.JWT_BEARER)) {
            return authHeader.substring(JwtTokenProvider.JWT_BEARER.length());
        }

        return null;
    }

    private void toAuthentication(HttpServletRequest request, String email) {

        try {
            UserDetails userDetails = detailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new  UsernamePasswordAuthenticationToken (
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );


            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            logger.debug("Usuário autenticado: {}", email);
        } catch (Exception e) {
            logger.error("Erro ao autenticar usuário: {}", e.getMessage());
        }

    }
}
