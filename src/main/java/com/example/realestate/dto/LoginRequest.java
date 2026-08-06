package com.example.realnest.dto;

import lombok.Data;
import com.example.realnest.dto.LoginRequest;
@Data
public class LoginRequest {

    private String email;

    private String password;
}