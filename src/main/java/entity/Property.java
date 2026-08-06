package com.example.realestate.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "properties")
@Data

public class Property{


    @Id
    private String id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Positive(message = "Price must be greater than zero")
    private double price;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Location is required")
    private String location;

    // Add this
    private String imageUrl;

    @Field("ownerId")
    private String ownerId;
    private Boolean approved = false;

    // Getters and Setters
}