package sg.edu.nus.iss.server.security;

public class Authority {
    
    public static final String[] USER_AUTHORITIES = { "dreamer:read" };
    public static final String[] POWER_USER_AUTHORITIES = { "dreamer:read", "dreamer:update" };
    public static final String[] ADMIN_AUTHORITIES = { "dreamer:read", "dreamer:update", "dreamer:create" };
    public static final String[] SUPER_ADMIN_AUTHORITIES = { "dreamer:read", "dreamer:update", "dreamer:create", "dreamer:delete" };
}
