package com.example.footprint.domain.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {
    @Email
    @NotBlank
    public String email;

    @Size(min = 6, max = 72)
    public String password;
}
