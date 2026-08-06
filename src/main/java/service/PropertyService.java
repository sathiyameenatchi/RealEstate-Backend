package com.example.realestate.service;

import com.example.realestate.entity.Property;
import com.example.realestate.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    // Create Property
    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    // Get All Properties
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    // Get All Approved Properties
    public List<Property> getAllApprovedProperties() {
        return propertyRepository.findByApprovedTrue();
    }

    // Get Property by ID
    public Optional<Property> getPropertyById(String id) {
        return propertyRepository.findById(id);
    }
    // Get Properties by Owner
    public List<Property> getPropertiesByOwner(String ownerId) {

        System.out.println("SEARCH OWNER ID = [" + ownerId + "]");


        List<Property> allProperties =
                propertyRepository.findAll();


        System.out.println(
                "ALL PROPERTIES COUNT = "
                        + allProperties.size()
        );


        System.out.println(
                "ALL PROPERTIES = "
                        + allProperties
        );


        List<Property> result =
                propertyRepository.findByOwnerId(ownerId);


        System.out.println(
                "MATCH RESULT = " + result
        );


        return result;
    }

    // Search by Location
    public List<Property> searchByLocation(String location) {
        return propertyRepository.findByLocationIgnoreCase(location);
    }

    // Search by Type
    public List<Property> searchByType(String type) {
        return propertyRepository.findByTypeIgnoreCase(type);
    }

    // Search by Price Range
    public List<Property> searchByPriceRange(double minPrice, double maxPrice) {
        return propertyRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // Update Property
    public Property updateProperty(String id, Property updatedProperty) {

        Optional<Property> existingProperty =
                propertyRepository.findById(id);

        if (existingProperty.isPresent()) {

            Property property = existingProperty.get();

            property.setTitle(updatedProperty.getTitle());
            property.setDescription(updatedProperty.getDescription());
            property.setPrice(updatedProperty.getPrice());
            property.setType(updatedProperty.getType());
            property.setLocation(updatedProperty.getLocation());
            property.setImageUrl(updatedProperty.getImageUrl());

            return propertyRepository.save(property);
        }

        throw new RuntimeException("Property not found");
    }
    // Delete Property
    public void deleteProperty(String id) {

        if (!propertyRepository.existsById(id)) {
            throw new RuntimeException("Property not found with id: " + id);
        }

        propertyRepository.deleteById(id);
    }

    // Check Property Ownership
    public boolean isOwner(String propertyId, String ownerId) {

        Property property = propertyRepository
                .findById(propertyId)
                .orElse(null);

        if (property == null) {
            System.out.println("PROPERTY NOT FOUND");
            return false;
        }
        System.out.println("PROPERTY OWNER ID : " + property.getOwnerId());
        System.out.println("LOGIN USER ID : " + ownerId);

        return property.getOwnerId().equals(ownerId);
    }

    // Approve Property
    public Property approveProperty(String id) {

        Optional<Property> existingProperty =
                propertyRepository.findById(id);

        if (existingProperty.isPresent()) {

            Property property = existingProperty.get();

            property.setApproved(true);

            return propertyRepository.save(property);
        }

        throw new RuntimeException("Property not found");
    }
}