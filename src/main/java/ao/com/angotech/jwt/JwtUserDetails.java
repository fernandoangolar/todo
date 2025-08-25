package ao.com.angotech.jwt;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class JwtUserDetails extends User {

    private ao.com.angotech.entity.User user;

    public JwtUserDetails(ao.com.angotech.entity.User user) {
        super(user.getEmail(), user.getPassword(), null);
        this.user = user;
    }

    public Long getId() {
        return this.user.getId();
    }
}
