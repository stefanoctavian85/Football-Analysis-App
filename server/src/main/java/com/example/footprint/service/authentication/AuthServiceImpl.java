package com.example.footprint.service.authentication;

import com.example.footprint.domain.dto.LoginDto;
import com.example.footprint.domain.dto.RegisterDto;
import com.example.footprint.domain.dto.UserDto;
import com.example.footprint.domain.entity.ERole;
import com.example.footprint.domain.entity.RefreshToken;
import com.example.footprint.domain.entity.Role;
import com.example.footprint.domain.entity.User;

import com.example.footprint.repository.RefreshTokenRepository;
import com.example.footprint.repository.RoleRepository;
import com.example.footprint.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public UserDto register(RegisterDto request, HttpServletResponse response) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists!");
        }

        Role userRole = roleRepository.findByRole(ERole.USER)
                .orElseThrow(() -> new IllegalArgumentException("Default role doesn't exists!"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(userRole);
        user.setCreatedAt(new Date());

        userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setRole(user.getRole().getRole());

        String accessToken = jwtService.generateToken(user.getEmail(), user.getRole());
        userDto.setToken(accessToken);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setAttribute("SameSite", "Lax");
        cookie.setMaxAge(Math.toIntExact(refreshTokenService.REFRESH_TOKEN_DURATION));
        response.addCookie(cookie);

        return userDto;
    }

    @Override
    public UserDto login(LoginDto request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials!");
        }

        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setRole(user.getRole().getRole());

        String accessToken = jwtService.generateToken(user.getEmail(), user.getRole());
        userDto.setToken(accessToken);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setAttribute("SameSite", "Lax");
        cookie.setMaxAge(Math.toIntExact(refreshTokenService.REFRESH_TOKEN_DURATION));
        response.addCookie(cookie);

        return userDto;
    }

    @Override
    public void logout(String refreshToken) {
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token!"));

        refreshTokenRepository.delete(tokenEntity);
    }
}
