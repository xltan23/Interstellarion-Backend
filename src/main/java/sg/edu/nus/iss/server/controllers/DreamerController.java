package sg.edu.nus.iss.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.server.exceptions.EmailExistsException;
import sg.edu.nus.iss.server.exceptions.EmailNotFoundException;
import sg.edu.nus.iss.server.exceptions.ExceptionHandling;
import sg.edu.nus.iss.server.models.Dreamer;
import sg.edu.nus.iss.server.security.DreamerPrincipal;
import sg.edu.nus.iss.server.security.JWTTokenProvider;
import sg.edu.nus.iss.server.services.DreamerService;

import static sg.edu.nus.iss.server.security.SecurityConstant.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping(value = {"/","/dreamer"})
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

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDreamer(@RequestBody Dreamer dreamer)  throws EmailNotFoundException {        
        System.out.println("Authenticating credentials...");
        authenticate(dreamer.getEmail(), dreamer.getPassword());
        dreamerSvc.delete(dreamer.getEmail());
        JsonObject deleteSuccess = Json.createObjectBuilder()
                                        .add("SUCCESS", "Dreamer (%s) deleted".formatted(dreamer.getEmail()))
                                        .build();
        return new ResponseEntity<>(deleteSuccess.toString(), HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editDreamer(@RequestBody Dreamer dreamer) throws EmailNotFoundException {
        dreamerSvc.updateProfile(dreamer.getFirstName(), dreamer.getLastName(), dreamer.getEmail(), dreamer.getProfileImageUrl());
        JsonObject editSuccess = Json.createObjectBuilder()
                                    .add("SUCCESS", "Dreamer (%s) edited".formatted(dreamer.getEmail()))
                                    .build();
        return new ResponseEntity<>(editSuccess.toString(), HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam("email") String email, @RequestParam("password") String password, 
                                                @RequestParam("newPassword") String newPassword) throws EmailNotFoundException {
        System.out.println("Authenticating credentials...");
        authenticate(email, password);
        dreamerSvc.changePassword(email, newPassword);
        JsonObject changeSuccess = Json.createObjectBuilder()
                                        .add("SUCCESS", "Dreamer (%s) password changed".formatted(email))
                                        .build();
        return new ResponseEntity<>(changeSuccess.toString(), HttpStatus.OK);
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody Dreamer dreamer) throws EmailNotFoundException, MessagingException {
        dreamerSvc.forgetPassword(dreamer.getEmail());
        JsonObject changeSuccess = Json.createObjectBuilder()
                                        .add("SUCCESS", "New password sent to %s. Please login and change your password".formatted(dreamer.getEmail()))
                                        .build();
        return new ResponseEntity<>(changeSuccess.toString(), HttpStatus.OK);
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

