package com.example.footprint.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDto {
    @Email
    @NotBlank
    String email;

    @Size(min = 6, max = 72)
    String password;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;
}
