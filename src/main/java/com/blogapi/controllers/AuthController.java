package com.blogapi.controllers;

import com.blogapi.config.security.JwtHelper;
import com.blogapi.entities.User;
import com.blogapi.exceptions.ResourceNotFoundException;
import com.blogapi.repositories.UserRepository;
import com.blogapi.response.LoginRequest;
import com.blogapi.response.AuthResponse;
import com.blogapi.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private JwtHelper jwtHelper;
    private UserService userService;
    private ModelMapper modelMapper;
    private UserRepository userRepository;


    public AuthController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager, JwtHelper jwtHelper, UserService userService, ModelMapper modelMapper, UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtHelper = jwtHelper;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        try {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

            authenticationManager.authenticate(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String accessToken = jwtHelper.generateAccessToken(userDetails);
            String refreshToken = jwtHelper.generateRefreshToken(userDetails);

            User user = userRepository.findByEmail(loginRequest.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User not found"));



            AuthResponse authResponse = new AuthResponse(
                    accessToken,
                    refreshToken,
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    user.getAge(),
                    user.getGender()


            );
            return new ResponseEntity<>(authResponse, HttpStatus.OK);

        } catch (BadCredentialsException bd) {
            System.out.println("Invalid Credentials");
            return ResponseEntity.badRequest().build();
        }
    }

    // Refresh access token
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        logger.info("refreshToken : {} ", refreshToken);
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token is missing");
        }

        try {
            // Check if it is a valid refresh token
            if (!jwtHelper.isRefreshToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid token type");
            }
            // Parse token and get username
            String username = jwtHelper.getUsernameFromToken(refreshToken);
            logger.info("username : {} ", username);
            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            logger.info("userDetails : {} ", userDetails);
            // Validate refresh token
            if (!jwtHelper.isValidToken(refreshToken, userDetails)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh token is expired or invalid");
            }

            // Generate new access token
            String newAccessToken = jwtHelper.generateAccessToken(userDetails);
            logger.info("newAccessToken : {} ", newAccessToken);
            // Optionally, you can also generate a new refresh token (rotate refresh token)
            return ResponseEntity.ok(Map.of(
                    "accessToken", newAccessToken,
                    "refreshToken", refreshToken
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid refresh token");
        }
    }


}
