package com.example.footprint.domain.dto;

import com.example.footprint.domain.entity.ERole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDto {
    String email;
    String firstName;
    String lastName;
    ERole role;
    String token;
}
