package sg.edu.nus.iss.server.security;

public class SecurityConstant {

    public static final long EXPIRATION_TIME = 432_000_000; 
    public static final String INTERSTELLARION = "Interstellarion";
    public static final String INTERSTELLARION_ADMINISTRATION = "Dreamer Management Portal";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String AUTHORITIES = "Authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to login to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    // public static final String[] PUBLIC_URLS = {"/dreamer/login", "/dreamer/register", "/dreamer/resetpassword/**"};
    public static final String[] PUBLIC_URLS = {"/**"}; 
}
