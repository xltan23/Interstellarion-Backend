package sg.edu.nus.iss.server.services;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import sg.edu.nus.iss.server.exceptions.EmailExistsException;
import sg.edu.nus.iss.server.exceptions.EmailNotFoundException;
import sg.edu.nus.iss.server.models.Dreamer;
import sg.edu.nus.iss.server.repositories.DreamerRepository;
import sg.edu.nus.iss.server.security.DreamerPrincipal;
import sg.edu.nus.iss.server.security.Role;

import javax.transaction.Transactional;

@Service
@Transactional
@Qualifier("userDetailsService")
public class DreamerService implements UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private DreamerRepository dreamerRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public DreamerService(DreamerRepository dreamerRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.dreamerRepo = dreamerRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Dreamer dreamer = findDreamerByEmail(email);
        if (null == dreamer) {
            LOGGER.error("User not found by email: " + email);
            throw new UsernameNotFoundException("Dreamer not found by email: " + email);
        } else {
            dreamer.setLastLoginDateDisplay(dreamer.getLastLoginDate());
            dreamer.setLastLoginDate(new Date());
            update(dreamer);
            // Set authority (Needs a method to match Dreamer's role to get authority)
            dreamer.setAuthorities(Role.ROLE_USER.getAuthorities());
            DreamerPrincipal dreamerPrincipal = new DreamerPrincipal(dreamer);
            LOGGER.info("Returning found user by email: " + email);
            return dreamerPrincipal;
        }
    }

    public Dreamer register(String firstName, String lastName, String email, String dateOfBirth, String gender) throws EmailExistsException {
        if (emailExists(email)) {
            throw new EmailExistsException("Email is already linked to another Dreamer");
        }
        // Define all attributes of the newly registered Dreamer
        Dreamer dreamer = new Dreamer();
        dreamer.setDreamerId(generateUserId());
        dreamer.setFirstName(firstName);
        dreamer.setLastName(lastName);
        dreamer.setEmail(email);
        String password = generatePassword();
        String encodedPassword = encodePassword(password);    
        dreamer.setPassword(encodedPassword);
        dreamer.setProfileImageUrl("");
        dreamer.setDateOfBirth(dateOfBirth);
        dreamer.setGender(gender);
        dreamer.setJoinDate(new Date());
        dreamer.setRole(Role.ROLE_USER.name());
        dreamer.setAuthorities(Role.ROLE_USER.getAuthorities());
        dreamer.setIsActive(true);
        dreamer.setIsNotLocked(true);
        // Last log in and display not defined as user yet to log in
        register(dreamer);
        System.out.println("Password: " + password);
        LOGGER.info("New user password: " + password);
        return dreamer;
    }

    private String getTemporaryProfileImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/").toUriString();
    }

    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private boolean emailExists(String email) throws EmailExistsException {
        Dreamer dreamer = findDreamerByEmail(email);
        if (null == dreamer) {
            return false;
        }
        return true;
    }

    // Repository methods
    public Dreamer findDreamerByEmail(String email) {
        return dreamerRepo.findDreamerByEmail(email);
    }

    private void register(Dreamer dreamer) {
        dreamerRepo.register(dreamer);
    }

    private void update(Dreamer dreamer) {
        dreamerRepo.update(dreamer);
    }
}
