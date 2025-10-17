package com.rishipadala.journalApp.Service;

import com.rishipadala.journalApp.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoogleAuthService {

    // Injecting OAuth2 client properties from application.yml
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Authenticates a user using the authorization code from Google.
     *
     * @param code The authorization code.
     * @return A JWT token for the authenticated or newly created user.
     * @throws Exception if authentication fails.
     */
    public String authenticate(String code) throws Exception {
        String accessToken = getAccessToken(code);
        String userEmail = getUserEmail(accessToken);

        // findOrCreateGoogleUser will check if the user exists and create them if they don't
        userService.findOrCreateGoogleUser(userEmail);

        // Generate a JWT token for the user
        return jwtUtil.generateToken(userEmail);
    }

    /**
     * Exchanges the authorization code for a Google access token.
     *
     * @param code The authorization code.
     * @return The access token.
     */
    private String getAccessToken(String code) {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, requestEntity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        }
        throw new RuntimeException("Failed to obtain access token from Google");
    }

    /**
     * Fetches user information from Google using the access token and extracts the email.
     *
     * @param accessToken The Google access token.
     * @return The user's email address.
     */
    private String getUserEmail(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<Map> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("email");
        }
        throw new RuntimeException("Failed to fetch user info from Google");
    }
}
