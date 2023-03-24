package sg.edu.nus.iss.server.security;

import static sg.edu.nus.iss.server.security.Authority.*;

public enum Role {
    
    ROLE_USER(USER_AUTHORITIES),
    ROLE_POWER_USER(POWER_USER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES),
    ROLE_SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES);

    // Each Role has set of authorities
    private String[] authorities;

    // Defined such that each role
    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
