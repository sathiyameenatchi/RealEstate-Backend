package com.example.realestate.controller;

import com.example.realestate.entity.Property;
import com.example.realestate.entity.User;
import com.example.realestate.service.PropertyService;
import com.example.realestate.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final PropertyService propertyService;

    public AdminController(UserService userService,
                           PropertyService propertyService) {
        this.userService = userService;
        this.propertyService = propertyService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/properties")
    public List<Property> getAllProperties() {

        return propertyService.getAllProperties();
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve/{id}")
    public Property approveProperty(@PathVariable String id) {
        return propertyService.approveProperty(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return "User Deleted Successfully";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/property/{id}")
    public String deleteProperty(@PathVariable String id) {
        propertyService.deleteProperty(id);
        return "Property Deleted Successfully";
    }
}