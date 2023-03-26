package sg.edu.nus.iss.server.security.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import sg.edu.nus.iss.server.services.LoginAttemptService;

@Component
public class AuthenticationFailureListener {
    
    private LoginAttemptService loginAttemptSvc;

    @Autowired
    public AuthenticationFailureListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptSvc = loginAttemptService;
    }

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        // Safe check 
        if (principal instanceof String) {
            String email = (String) event.getAuthentication().getPrincipal();
            loginAttemptSvc.addUserToLoginAttemptCache(email);
        }
    }
}
