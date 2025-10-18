package com.rishipadala.journalApp.Controller;

import com.rishipadala.journalApp.Service.GoogleAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;


@RestController
@Slf4j
@RequestMapping("/auth/google")
@Tag(name = "Google Authentication", description = "APIs for Google OAuth2 Login/Signup")
public class GoogleAuthController {

    @Autowired
    private GoogleAuthService googleAuthService;

    /**
     * This is the callback endpoint that Google redirects to after a user grants permission.
     * It receives an authorization code which is then exchanged for an access token and user info.
     *
     * @param code The authorization code provided by Google.
     * @return A ResponseEntity containing a JWT token on success, or an error status on failure.
     */
    @GetMapping("/callback")
    @Operation(summary = "Handle Google OAuth2 callback and authenticate user")
    public ResponseEntity<Map<String, String>> handleGoogleCallback(@RequestParam String code) {
        try {
            String jwtToken = googleAuthService.authenticate(code);
            return ResponseEntity.ok(Collections.singletonMap("token", jwtToken));
        } catch (Exception e) {
            // It's good practice to log the exception here
            log.error("Exception occurred during Google callback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
