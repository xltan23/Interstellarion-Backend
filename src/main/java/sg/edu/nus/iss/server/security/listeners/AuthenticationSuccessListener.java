package sg.edu.nus.iss.server.security.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import sg.edu.nus.iss.server.security.DreamerPrincipal;
import sg.edu.nus.iss.server.services.LoginAttemptService;

@Component
public class AuthenticationSuccessListener {
    
    private LoginAttemptService loginAttemptSvc;

    @Autowired
    public AuthenticationSuccessListener(LoginAttemptService loginAttemptSvc) {
        this.loginAttemptSvc = loginAttemptSvc;
    }

    @EventListener
    public void authenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof DreamerPrincipal) {
            DreamerPrincipal dreamer = (DreamerPrincipal) event.getAuthentication().getPrincipal();
            loginAttemptSvc.removeUserFromLoginAttemptCache(dreamer.getUsername());
        }
    }
}
