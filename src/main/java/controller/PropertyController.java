package com.example.realestate.controller;

import com.example.realestate.entity.Property;
import com.example.realestate.entity.User;
import com.example.realestate.service.PropertyService;
import com.example.realestate.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.realestate.repository.PropertyRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/properties")
public class PropertyController {


    private final PropertyService propertyService;
    private final UserService userService;
    private final PropertyRepository propertyRepository;


    public PropertyController(
            PropertyService propertyService,
            UserService userService,
            PropertyRepository propertyRepository) {

        this.propertyService = propertyService;
        this.userService = userService;
        this.propertyRepository = propertyRepository;
    }


    // CREATE PROPERTY

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<Property> createProperty(
            @Valid @RequestBody Property property,
            Authentication authentication) {


        User user =
                userService.getUserByEmail(
                        authentication.getName()
                );


        property.setOwnerId(user.getId());

        property.setApproved(false);


        return ResponseEntity.ok(
                propertyService.createProperty(property)
        );
    }


    // GET ALL PROPERTIES

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {

        return ResponseEntity.ok(
                propertyService.getAllApprovedProperties()
        );
    }


    // GET PROPERTY BY ID

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(
            @PathVariable String id) {

        return propertyService.getPropertyById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }


    // MY PROPERTIES

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/my")
    public ResponseEntity<?> myProperties(
            Authentication authentication) {


        try {


            if (authentication == null) {

                return ResponseEntity
                        .status(401)
                        .body("Please login first");
            }


            User user =
                    userService.getUserByEmail(
                            authentication.getName()
                    );


            if (user == null) {

                return ResponseEntity
                        .status(404)
                        .body("User not found");
            }


            System.out.println(
                    "LOGIN EMAIL : "
                            + authentication.getName()
            );


            System.out.println(
                    "USER ID : "
                            + user.getId()
            );


            // TEST MAPPING

            List<Property> all =
                    propertyRepository.findAll();


            System.out.println(
                    "ALL PROPERTIES : "
                            + all
            );


            if (!all.isEmpty()) {

                System.out.println(
                        "FIRST OWNER ID : "
                                + all.get(0).getOwnerId()
                );
            }


            List<Property> properties =
                    propertyService.getPropertiesByOwner(
                            user.getId()
                    );


            System.out.println(
                    "PROPERTY COUNT : "
                            + properties.size()
            );


            System.out.println(
                    "PROPERTIES : "
                            + properties
            );


            return ResponseEntity.ok(properties);


        } catch (Exception e) {


            e.printStackTrace();


            return ResponseEntity
                    .status(500)
                    .body(e.getMessage());
        }

    }


    // GET BY OWNER

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Property>> getByOwner(
            @PathVariable String ownerId) {


        return ResponseEntity.ok(
                propertyService.getPropertiesByOwner(ownerId)
        );
    }


    // SEARCH LOCATION

    @GetMapping("/search/location")
    public ResponseEntity<List<Property>> searchByLocation(
            @RequestParam String location) {


        return ResponseEntity.ok(
                propertyService.searchByLocation(location)
        );
    }


    // SEARCH TYPE

    @GetMapping("/search/type")
    public ResponseEntity<List<Property>> searchByType(
            @RequestParam String type) {


        return ResponseEntity.ok(
                propertyService.searchByType(type)
        );
    }


    // SEARCH PRICE

    @GetMapping("/search/price")
    public ResponseEntity<List<Property>> searchByPrice(
            @RequestParam double minPrice,
            @RequestParam double maxPrice) {


        return ResponseEntity.ok(
                propertyService.searchByPriceRange(
                        minPrice,
                        maxPrice
                )
        );
    }


    // UPDATE PROPERTY

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProperty(
            @PathVariable String id,
            @RequestBody Property property,
            Authentication authentication) {


        User user =
                userService.getUserByEmail(
                        authentication.getName()
                );


        if (!propertyService.isOwner(
                id,
                user.getId())) {


            return ResponseEntity
                    .badRequest()
                    .body("You are not owner");
        }


        return ResponseEntity.ok(
                propertyService.updateProperty(
                        id,
                        property
                )
        );
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(
            @PathVariable String id,
            Authentication authentication) {

        User user = userService.getUserByEmail(
                authentication.getName()
        );


        System.out.println("========== DELETE DEBUG ==========");
        System.out.println("PROPERTY ID : " + id);
        System.out.println("LOGIN EMAIL : " + authentication.getName());
        System.out.println("LOGIN USER ID : " + user.getId());


        boolean owner = propertyService.isOwner(
                id,
                user.getId()
        );


        System.out.println("IS OWNER : " + owner);
        System.out.println("==================================");


        if (!owner) {

            return ResponseEntity
                    .badRequest()
                    .body("You are not the owner");
        }


        propertyService.deleteProperty(id);


        return ResponseEntity.ok(
                "Property Deleted Successfully"
        );
    }
}




