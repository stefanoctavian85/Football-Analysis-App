package com.example.footprint.config;

import com.example.footprint.domain.entity.user.ERole;
import com.example.footprint.domain.entity.user.Role;
import com.example.footprint.domain.entity.user.User;
import com.example.footprint.repository.user.RoleRepository;
import com.example.footprint.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class RuntimeConfig {
    @Value("${admin.email}")
    private String ADMIN_EMAIL;
    @Value("${admin.password}")
    private String ADMIN_PASSWORD;

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public Role setUserRoles() {
        Optional<Role> existingUserRole = roleRepository.findByRole(ERole.ROLE_USER);

        return existingUserRole.orElseGet(() -> {
            Role userRole = new Role();
            userRole.setRole(ERole.ROLE_USER);
            roleRepository.save(userRole);
            return userRole;
        });
    }

    @Bean
    public Role setAdminRole() {
        Optional<Role> existingAdminRole = roleRepository.findByRole(ERole.ROLE_ADMIN);

        return existingAdminRole.orElseGet(() -> {
            Role adminRole = new Role();
            adminRole.setRole(ERole.ROLE_ADMIN);
            roleRepository.save(adminRole);
            return adminRole;
        });
    }

    @Bean
    public User setAdminUser() {
        Optional<User> existingAdminUser = userRepository.findByEmail(ADMIN_EMAIL);

        return existingAdminUser.orElseGet(() -> {
            User adminUser = new User();
            adminUser.setEmail(ADMIN_EMAIL);
            adminUser.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            adminUser.setFirstName("admin_firstname");
            adminUser.setLastName("admin_lastname");
            adminUser.setRole(setAdminRole());
            adminUser.setCreatedAt(new Date());
            userRepository.save(adminUser);
            return adminUser;
        });
    }
}
