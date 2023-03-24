package sg.edu.nus.iss.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.server.exceptions.EmailExistsException;
import sg.edu.nus.iss.server.exceptions.EmailNotFoundException;
import sg.edu.nus.iss.server.exceptions.ExceptionHandling;
import sg.edu.nus.iss.server.models.Dreamer;
import sg.edu.nus.iss.server.security.DreamerPrincipal;
import sg.edu.nus.iss.server.security.JWTTokenProvider;
import sg.edu.nus.iss.server.security.Role;
import sg.edu.nus.iss.server.services.DreamerService;

import static sg.edu.nus.iss.server.security.SecurityConstant.*;

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

    @PostMapping("/login")
    public ResponseEntity<Dreamer> login(@RequestBody Dreamer dreamer) {
        System.out.println("Authenticating credentials...");
        authenticate(dreamer.getEmail(), dreamer.getPassword());
        Dreamer loginDreamer = dreamerSvc.findDreamerByEmail(dreamer.getEmail());
        // Set authority (Needs a method to match Dreamer's role to get authority)
        loginDreamer.setAuthorities(Role.ROLE_ADMIN.getAuthorities());
        DreamerPrincipal dreamerPrincipal = new DreamerPrincipal(loginDreamer);
        System.out.println("Building Http Headers...");
        HttpHeaders jwtHeaders = getJwtHeader(dreamerPrincipal);
        System.out.println("Headers complete");
        return new ResponseEntity<>(loginDreamer, jwtHeaders, HttpStatus.OK);
    }
    
    @PostMapping("/register")
    public ResponseEntity<Dreamer> register(@RequestBody Dreamer dreamer) throws EmailNotFoundException, EmailExistsException {
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

