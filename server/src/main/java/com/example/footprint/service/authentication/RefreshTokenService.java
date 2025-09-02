package com.example.footprint.service.authentication;

import com.example.footprint.domain.entity.RefreshToken;
import com.example.footprint.domain.entity.User;
import com.example.footprint.repository.RefreshTokenRepository;
import com.example.footprint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${spring.application.refreshExpirationMs}")
    protected Long REFRESH_TOKEN_DURATION;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshToken createRefreshToken(Long userId) {
        refreshTokenRepository.findByUserId(userId).ifPresent(refreshTokenRepository::delete);
        RefreshToken token = new RefreshToken();
        token.setUser(userRepository.findById(userId).get());
        token.setExpiryDate(new Date(System.currentTimeMillis() + REFRESH_TOKEN_DURATION));
        token.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(token);
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().before(new Date());
    }
}
