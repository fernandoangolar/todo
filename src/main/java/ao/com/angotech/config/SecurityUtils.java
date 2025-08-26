package ao.com.angotech.config;

import ao.com.angotech.jwt.JwtUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {

    }

    /**
     * Obtém o usuário autenticado atual
     * @return JwtUserDetails do usuário autenticado ou null se não autenticado
     */
    public static JwtUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
            return (JwtUserDetails) authentication.getPrincipal();
        }

        return null;
    }

    /**
     * Obtém o ID do usuário autenticado atual
     * @return ID do usuário ou null se não autenticado
     */
    public static Long getCurrentUserId() {
        JwtUserDetails user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    /**
     * Obtém o email do usuário autenticado atual
     * @return Email do usuário ou null se não autenticado
     */
    public static String getCurrentUserEmail() {
        JwtUserDetails user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }

    /**
     * Verifica se há um usuário autenticado
     * @return true se há usuário autenticado, false caso contrário
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof JwtUserDetails;
    }
}
