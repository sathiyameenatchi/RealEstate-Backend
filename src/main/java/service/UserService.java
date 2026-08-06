package com.example.realestate.service;

import com.example.realestate.entity.User;
import com.example.realestate.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {


    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // Create User
    public User createUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        return userRepository.save(user);
    }

    // Get User by Email
    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    // Get User by ID
    public Optional<User> getUserById(String id) {

        return userRepository.findById(id);
    }


    // Get All Users
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }


    // Delete User
    public void deleteUser(String id) {

        userRepository.deleteById(id);
    }
    }