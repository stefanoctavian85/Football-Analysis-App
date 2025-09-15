package com.example.footprint.controller;

import com.example.footprint.domain.dto.user.LoginDto;
import com.example.footprint.domain.dto.user.RegisterDto;
import com.example.footprint.domain.dto.user.UserDto;
import com.example.footprint.repository.user.RefreshTokenRepository;
import com.example.footprint.service.authentication.AuthService;
import com.example.footprint.service.authentication.JwtService;
import com.example.footprint.service.authentication.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto request, HttpServletResponse response) {
        UserDto userDto = authService.login(request, response);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterDto request, HttpServletResponse response) {
        UserDto userDto = authService.register(request, response);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Refresh token is missing!"));
        }

        return refreshTokenRepository.findByToken(refreshToken)
                .map(token -> {
                    if (refreshTokenService.isTokenExpired(token)) {
                        refreshTokenRepository.delete(token);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                                Map.of("message", "Refresh token expired!"));
                    }

                    String accessToken = jwtService.generateToken(token.getUser().getEmail(), token.getUser().getRole());
                    UserDto user = new UserDto();
                    user.setEmail(token.getUser().getEmail());
                    user.setFirstName(token.getUser().getFirstName());
                    user.setLastName(token.getUser().getLastName());
                    user.setRole(token.getUser().getRole().getRole());

                    Map<String, Object> response = new HashMap<>();
                    response.put("accessToken", accessToken);
                    response.put("user", user);
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        Map.of("message", "Refresh token not found!")));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", "Refresh token is missing!"));
        }

        authService.logout(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .domain("localhost")
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logout successfully!");
    }
}
