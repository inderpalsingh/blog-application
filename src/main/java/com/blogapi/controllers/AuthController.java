package com.blogapi.controllers;

import com.blogapi.config.security.JwtHelper;
import com.blogapi.dto.UserDto;
import com.blogapi.entities.User;
import com.blogapi.exceptions.ResourceNotFoundException;
import com.blogapi.repositories.UserRepository;
import com.blogapi.response.LoginRequest;
import com.blogapi.response.LoginResponse;
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

    private Logger logger = LoggerFactory.getLogger(AuthController.class);



    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){

        try{
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

            authenticationManager.authenticate(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String accessToken = jwtHelper.generateAccessToken(userDetails);
            String refreshToken = jwtHelper.generateRefreshToken(userDetails);

            User user = userRepository.findByEmail(loginRequest.getUsername()).orElseThrow(()-> new ResourceNotFoundException("User not found"));

            LoginResponse loginResponse = new LoginResponse(
                    accessToken,
                    refreshToken,
                    user.getEmail()


            );
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);

        } catch (BadCredentialsException bd) {
            System.out.println("Invalid Credentials");
            return ResponseEntity.badRequest().build();
        }
    }





}
