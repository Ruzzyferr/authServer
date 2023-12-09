package com.example.authserver.dto;

import com.example.authserver.enums.Role;
import lombok.Data;

@Data
public class LoginBackDto {
    private int id;

    private Role role;

    private String token;
}
