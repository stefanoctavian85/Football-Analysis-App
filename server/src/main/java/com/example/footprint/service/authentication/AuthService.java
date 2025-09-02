package com.example.footprint.service.authentication;

import com.example.footprint.domain.dto.LoginDto;
import com.example.footprint.domain.dto.RegisterDto;
import com.example.footprint.domain.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    UserDto register(RegisterDto request, HttpServletResponse response);
    UserDto login(LoginDto request, HttpServletResponse response);
    void logout(String refreshToken);
}
