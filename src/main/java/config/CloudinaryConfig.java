package com.example.realestate.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {

        Map<String, String> config =
                new HashMap<>();

        config.put(
                "cloud_name",
                "vzninrsa"
                );

        config.put(
                "api_key",
                "432195343945194"
        );

        config.put(
                "api_secret",
                "XQ9Pd-brymB_WKRgfg2jkOnh898"
        );


        return new Cloudinary(config);
    }
}