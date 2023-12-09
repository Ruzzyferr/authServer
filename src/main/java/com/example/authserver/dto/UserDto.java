package com.example.authserver.dto;

import com.example.authserver.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserDto {

    private int id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isActive;

}
