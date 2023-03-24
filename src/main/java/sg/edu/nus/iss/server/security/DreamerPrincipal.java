package sg.edu.nus.iss.server.security;

import java.util.Collection;
import java.util.stream.Collectors;
import static java.util.Arrays.stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import sg.edu.nus.iss.server.models.Dreamer;

public class DreamerPrincipal implements UserDetails {
    
    private Dreamer dreamer;

    public DreamerPrincipal(Dreamer dreamer) {
        this.dreamer = dreamer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Map all String authorities into new object of class SimpleGrantedAuthority
        System.out.println("Getting authorities...");
        for (String authority : this.dreamer.getAuthorities()) {
            System.out.println(authority);
        }
        return stream(this.dreamer.getAuthorities()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.dreamer.getPassword();
    }

    @Override 
    public String getUsername() {
        return this.dreamer.getEmail();
    }

    // True to make sure user always able to login
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.dreamer.getIsNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.dreamer.getIsActive();
    }
}
