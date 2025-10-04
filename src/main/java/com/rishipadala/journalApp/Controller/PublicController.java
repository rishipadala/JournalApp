package com.rishipadala.journalApp.Controller;

import com.rishipadala.journalApp.Entity.User;
import com.rishipadala.journalApp.Service.UserDetailsServiceImpl;
import com.rishipadala.journalApp.Service.UserService;
import com.rishipadala.journalApp.dto.UserDTO;
import com.rishipadala.journalApp.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
@Tag(name = "Public APIs", description = "These APIs are for Login & Signup")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/health-check")
    @Operation(summary = "Check the health status of the application")
    public String healthCheck(){
        return "OK";
    }

    @PostMapping("/signup")
    @Operation(summary = "Create a new user account")
    public void signup(@RequestBody UserDTO user){
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setSentimentAnalysis(user.isSentimentAnalysis());
        userService.saveNewUser(newUser);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user and receive a JWT token")
    public ResponseEntity<String> login(@RequestBody User user){

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error("Exception occured while createAuthenticationToken");
            return new ResponseEntity<>("Incorrect Username or Password", HttpStatus.BAD_REQUEST);
        }
    }
}
