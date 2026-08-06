package com.example.realestate.controller;

import com.example.realestate.entity.User;
import com.example.realestate.security.JwtService;
import com.example.realestate.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Map;
import java.util.HashMap;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthController(
            UserService userService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    // Register User
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {

        try {

            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("CUSTOMER");
            }

            user.setPassword(
                    passwordEncoder.encode(user.getPassword())
            );

            User savedUser = userService.createUser(user);

            savedUser.setPassword(null);

            return new ResponseEntity<>(
                    savedUser,
                    HttpStatus.CREATED
            );

        } catch (RuntimeException e) {

            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // Login User
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            user.getPassword()
                    )
            );


            User loggedUser =
                    userService.getUserByEmail(
                            user.getEmail()
                    );

            String token =
                    jwtService.generateToken(
                            loggedUser
                    );


            Map<String, Object> response = new HashMap<>();

            response.put(
                    "token",
                    token
            );

            response.put(
                    "role",
                    loggedUser.getRole()
            );


            return ResponseEntity.ok(response);


        } catch (Exception e) {

            return new ResponseEntity<>(
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED
            );

        }
    }
}