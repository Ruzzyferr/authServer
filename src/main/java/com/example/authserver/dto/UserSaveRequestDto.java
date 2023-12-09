package com.example.authserver.dto;

import com.example.authserver.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.awt.*;

@Data
public class UserSaveRequestDto {

    private int id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isActive;
}
