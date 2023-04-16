package sg.edu.nus.iss.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.server.exceptions.EmailExistsException;
import sg.edu.nus.iss.server.exceptions.EmailNotFoundException;
import sg.edu.nus.iss.server.exceptions.ExceptionHandling;
import sg.edu.nus.iss.server.models.Dreamer;
import sg.edu.nus.iss.server.security.DreamerPrincipal;
import sg.edu.nus.iss.server.security.HttpResponse;
import sg.edu.nus.iss.server.security.JWTTokenProvider;
import sg.edu.nus.iss.server.services.DreamerService;

import static sg.edu.nus.iss.server.security.SecurityConstant.*;

import java.io.IOException;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.sql.rowset.serial.SerialException;

@RestController
@RequestMapping(value = {"/","/dreamer"})
@CrossOrigin(origins = "*")
public class DreamerController extends ExceptionHandling {

    private DreamerService dreamerSvc;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public DreamerController(DreamerService dreamerSvc, AuthenticationManager authenticationManager,
            JWTTokenProvider jwtTokenProvider) {
        this.dreamerSvc = dreamerSvc;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/image/{dreamerId}")
    public ResponseEntity<String> getProfileImage(@PathVariable String dreamerId) throws SQLException {
        System.out.println("Received by server:" + dreamerId);
        String base64String = dreamerSvc.getProfileImage(dreamerId);
        return new ResponseEntity<>(base64String, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<HttpResponse> deleteDreamer(@RequestPart String email, @RequestPart String password)  throws EmailNotFoundException {        
        System.out.println("Authenticating credentials...");
        authenticate(email, password);
        dreamerSvc.delete(email);
        String deleteMessage = "Dreamer (%s) deleted".formatted(email);
        return response(HttpStatus.OK, deleteMessage);
    }

    @PutMapping("/edit")
    public ResponseEntity<HttpResponse> editDreamer(@RequestPart String firstName, @RequestPart String lastName, 
                                                @RequestPart String email, @RequestPart MultipartFile profileImage) throws EmailNotFoundException, SerialException, SQLException, IOException {
        dreamerSvc.updateProfile(firstName, lastName, email, profileImage);
        String editMessage = "Dreamer (%s) edited. Please re-login to see the changes".formatted(email);
        return response(HttpStatus.OK, editMessage);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<HttpResponse> changePassword(@RequestParam("email") String email, @RequestParam("password") String password, 
                                                @RequestParam("newPassword") String newPassword) throws EmailNotFoundException {
        System.out.println("Authenticating credentials...");
        authenticate(email, password);
        dreamerSvc.changePassword(email, newPassword);
        String changeMessage = "Dreamer (%s) password changed".formatted(email);
        return response(HttpStatus.OK, changeMessage);
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<HttpResponse> resetPassword(@RequestBody Dreamer dreamer) throws EmailNotFoundException, MessagingException {
        dreamerSvc.forgetPassword(dreamer.getEmail());
        String resetMessage = "New password sent to %s. Please login and change your password".formatted(dreamer.getEmail());
        return response(HttpStatus.OK, resetMessage);
    }

    @PostMapping("/login")
    public ResponseEntity<Dreamer> login(@RequestBody Dreamer dreamer) {
        System.out.println("Authenticating credentials...");
        authenticate(dreamer.getEmail(), dreamer.getPassword());
        Dreamer loginDreamer = dreamerSvc.findDreamerByEmail(dreamer.getEmail());
        loginDreamer.setAuthorities(dreamerSvc.getAuthoritiesFromRole(loginDreamer.getRole()));
        DreamerPrincipal dreamerPrincipal = new DreamerPrincipal(loginDreamer);
        System.out.println("Building Http Headers...");
        HttpHeaders jwtHeaders = getJwtHeader(dreamerPrincipal);
        System.out.println("Headers complete");
        return new ResponseEntity<>(loginDreamer, jwtHeaders, HttpStatus.OK);
    }
    
    @PostMapping("/register")
    public ResponseEntity<Dreamer> register(@RequestBody Dreamer dreamer) throws EmailNotFoundException, EmailExistsException, MessagingException {
        Dreamer registerDreamer = dreamerSvc.register(dreamer.getFirstName(), dreamer.getLastName(), dreamer.getEmail(), dreamer.getDateOfBirth(), dreamer.getGender());
        return new ResponseEntity<>(registerDreamer, HttpStatus.OK);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase(), message), httpStatus);
    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        System.out.println("Authentication complete");
    }

    private HttpHeaders getJwtHeader(DreamerPrincipal dreamerPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(dreamerPrincipal));
        return headers;
    }
}

