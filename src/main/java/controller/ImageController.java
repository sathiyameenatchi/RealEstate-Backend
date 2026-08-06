package com.example.realestate.controller;

import com.example.realestate.service.ImageUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageUploadService
            ImageUploadService;

    public ImageController(
            ImageUploadService imageUploadService) {

        this.ImageUploadService =
                imageUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file")
            MultipartFile file) {

        try {

            String imageUrl =
                    ImageUploadService
                            .uploadImage(file);

            return ResponseEntity.ok(
                    Map.of(
                            "imageUrl",
                            imageUrl
                    )
            );

        } catch (IOException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "Image upload failed"
                    );
        }
    }
}