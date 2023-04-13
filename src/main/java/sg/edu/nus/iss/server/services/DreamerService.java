package sg.edu.nus.iss.server.services;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.server.exceptions.EmailExistsException;
import sg.edu.nus.iss.server.exceptions.EmailNotFoundException;
import sg.edu.nus.iss.server.models.Dreamer;
import sg.edu.nus.iss.server.repositories.DreamerRepository;
import sg.edu.nus.iss.server.security.DreamerPrincipal;
import sg.edu.nus.iss.server.security.Role;

import javax.mail.MessagingException;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import javax.transaction.Transactional;

@Service
@Transactional
@Qualifier("userDetailsService")
public class DreamerService implements UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private DreamerRepository dreamerRepo;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private LoginAttemptService loginAttemptSvc;
    private EmailService emailSvc;

    // CONSTRUCTOR
    @Autowired
    public DreamerService(DreamerRepository dreamerRepo, BCryptPasswordEncoder bCryptPasswordEncoder, LoginAttemptService loginAttemptSvc, EmailService emailSvc) {
        this.dreamerRepo = dreamerRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.loginAttemptSvc = loginAttemptSvc;
        this.emailSvc = emailSvc;
    }

    // METHOD: Called when AuthenticationManager authenticates
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find Dreamer from database
        Dreamer dreamer = findDreamerByEmail(email);
        if (null == dreamer) {
            LOGGER.error("User not found by email: " + email);
            throw new UsernameNotFoundException("Dreamer not found by email: " + email);
        } else {
            validateLoginAttempt(dreamer);
            // Since Dreamer is logging in,  Last Login is updated
            dreamer.setLastLoginDateDisplay(dreamer.getLastLoginDate());
            dreamer.setLastLoginDate(new Date());
            updateLastLogin(dreamer);
            dreamer.setAuthorities(getAuthoritiesFromRole(dreamer.getRole()));
            DreamerPrincipal dreamerPrincipal = new DreamerPrincipal(dreamer);
            LOGGER.info("Returning found user by email: " + email);
            return dreamerPrincipal;
        }
    }

    // METHOD: Register new Dreamer with details input
    public Dreamer register(String firstName, String lastName, String email, String dateOfBirth, String gender) throws EmailExistsException, MessagingException {
        // If email already exists in database
        if (emailExists(email)) {
            throw new EmailExistsException("Email is already linked to another Dreamer");
        }
        // Define all attributes of the newly registered Dreamer (Except Last Login)
        Dreamer dreamer = new Dreamer();
        dreamer.setDreamerId(generateUserId());
        dreamer.setFirstName(firstName);
        dreamer.setLastName(lastName);
        dreamer.setEmail(email);
        String password = generatePassword();
        String encodedPassword = encodePassword(password);    
        dreamer.setPassword(encodedPassword);
        dreamer.setProfileImageUrl(getTemporaryProfileImageUrl(dreamer.getFirstName()));
        dreamer.setDateOfBirth(dateOfBirth);
        dreamer.setGender(gender);
        dreamer.setJoinDate(new Date());
        dreamer.setRole(Role.ROLE_USER.name());
        dreamer.setAuthorities(Role.ROLE_USER.getAuthorities());
        dreamer.setIsActive(true);
        dreamer.setIsNotLocked(true);
        // Last log in and display not defined as Dreamer yet to log in
        register(dreamer);
        // Send password to Dreamer's email
        sendEmail(firstName, password, email);
        return dreamer;
    }

    // METHOD: Update existing Dreamer
    // Cannot update Email, DOB, Gender, Join Date 
    public Dreamer updateProfile(String newFirstName, String newLastName, String email, MultipartFile newProfileImage) throws EmailNotFoundException, SerialException, SQLException, IOException {
        // If email does not exist in database
        if (!emailExists(email)) {
            throw new EmailNotFoundException("No Dreamer found by email: " + email);
        }
        Dreamer dreamer = findDreamerByEmail(email);
        dreamer.setFirstName(newFirstName);
        dreamer.setLastName(newLastName);
        Blob blob = new SerialBlob(newProfileImage.getBytes());
        updateProfile(dreamer, blob);
        return dreamer;
    }

    // METHOD: Forget Password
    public void forgetPassword(String email) throws EmailNotFoundException, MessagingException {
        // If email does not exist in database
        if (!emailExists(email)) {
            throw new EmailNotFoundException("No Dreamer found by email: " + email);
        }
        Dreamer dreamer = findDreamerByEmail(email);
        // Create new password for the Dreamer
        String password = generatePassword();
        String encodedPassword = encodePassword(password); 
        dreamer.setPassword(encodedPassword);
        updatePassword(dreamer);
        sendEmail(dreamer.getFirstName(), password, email);
    }

    // METHOD: Change Password 
    public void changePassword(String email, String newPassword) throws EmailNotFoundException {
        // If email does not exist in database
        if (!emailExists(email)) {
            throw new EmailNotFoundException("No Dreamer found by email: " + email);
        }
        Dreamer dreamer = findDreamerByEmail(email);
        String newEncodedPassword = encodePassword(newPassword);
        dreamer.setPassword(newEncodedPassword);
        updatePassword(dreamer);
    }

    // METHOD: Delete Dreamer
    public void delete(String email) throws EmailNotFoundException {
        // If email does not exist in database
        if (!emailExists(email)) {
            throw new EmailNotFoundException("No Dreamer found by email: " + email);
        }
        Dreamer dreamer = findDreamerByEmail(email);
        delete(dreamer);
    }

    // METHOD: Get all authorities based on the user's role
    public String[] getAuthoritiesFromRole(String role) {
        if (role.equals("ROLE_POWER_USER")) {
            String[] authorities = Role.ROLE_POWER_USER.getAuthorities();
            return authorities;
        }
        if (role.equals("ROLE_ADMIN")) {
            String[] authorities = Role.ROLE_ADMIN.getAuthorities();
            return authorities;
        }
        if (role.equals("ROLE_SUPER_ADMIN")) {
            String[] authorities = Role.ROLE_SUPER_ADMIN.getAuthorities();
            return authorities;
        }
        String[] authorities = Role.ROLE_USER.getAuthorities();
        return authorities;
    }

    // METHOD: Get permanent Profile Image from database
    public String getProfileImage(String dreamerId) throws SQLException {
        Blob blob = dreamerRepo.getProfileImage(dreamerId);
        return convertBlobToBase64(blob);
    }

    // METHOD: Convert Blob to Base64 String
    private String convertBlobToBase64(Blob blob) throws SQLException {
        String base64String = "";
        if (blob != null) {
            byte[] blobBytes = blob.getBytes(1, (int)blob.length());
            base64String = Base64.getEncoder().encodeToString(blobBytes);
        }
        return base64String;
    }

    // METHOD: Get temporary Profile Image Url from robohash.org
    private String getTemporaryProfileImageUrl(String firstName) {
        return "https://robohash.org/%s".formatted(firstName);
    }

    // METHOD: Encode to conceal Password in database
    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    // METHOD: Generate random 10 alphanumeric as Password
    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    // METHOD: Generate random 10 numbers as User ID
    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    // METHOD: Check if email exists
    private boolean emailExists(String email) {
        Dreamer dreamer = findDreamerByEmail(email);
        if (null == dreamer) {
            return false;
        }
        return true;
    }

    // METHOD: Validate if Dreamer is not locked and locks the Dreamer if exceed login attempts
    private void validateLoginAttempt(Dreamer dreamer) {
        // If account is not locked, check whether user exceeds maximum attempts to login
        if (dreamer.getIsNotLocked()) {
            // If attempts exceeds 5 times, lock the account
            if (loginAttemptSvc.hasExceedMaxAttempt(dreamer.getEmail())) {
                dreamer.setIsNotLocked(false);
            // If attempts < 5, keep account unlocked
            } else {
                dreamer.setIsNotLocked(true);
            }
        // If account is locked forbid further attempts to login
        } else {
            loginAttemptSvc.removeUserFromLoginAttemptCache(dreamer.getEmail());
        }
    }

    // Repository and other Service methods
    public Dreamer findDreamerByEmail(String email) {
        return dreamerRepo.findDreamerByEmail(email);
    }

    private void register(Dreamer dreamer) {
        dreamerRepo.register(dreamer);
    }

    private void updateLastLogin(Dreamer dreamer) {
        dreamerRepo.updateLastLogin(dreamer);
    }

    private void updatePassword(Dreamer dreamer) {
        dreamerRepo.updatePassword(dreamer);
    }

    private void updateProfile(Dreamer dreamer, Blob blob) {
        dreamerRepo.updateProfile(dreamer, blob);
    }

    private void delete(Dreamer dreamer) {
        dreamerRepo.delete(dreamer);
    }

    private void sendEmail(String firstName, String password, String email) throws MessagingException {
        emailSvc.sendNewPasswordEmail(firstName, password, email);
    }
}
