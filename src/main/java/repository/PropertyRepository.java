package com.example.realestate.repository;

import com.example.realestate.entity.Property;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PropertyRepository extends MongoRepository<Property, String> {

    List<Property> findByLocationIgnoreCase(String location);

    List<Property> findByTypeIgnoreCase(String type);

    List<Property> findByOwnerId(String ownerId);

    List<Property> findByApprovedTrue();

    List<Property> findByPriceBetween(double minPrice, double maxPrice);
}