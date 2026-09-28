package dev.davisantos.TaskVaultApi.config;

import dev.davisantos.TaskVaultApi.database.model.BaseRoles;
import dev.davisantos.TaskVaultApi.database.model.RoleEntity;
import dev.davisantos.TaskVaultApi.database.model.UserEntity;
import dev.davisantos.TaskVaultApi.database.repository.RoleRepository;
import dev.davisantos.TaskVaultApi.database.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedRoles(RoleRepository roleRepository) {
        return args -> {
            for(BaseRoles role : BaseRoles.values()) {
                if(!roleRepository.existsByName(role.name())) {
                    roleRepository.save(RoleEntity.builder().name(role.name()).build());
                }
            }
        };
    }

    @Bean
    CommandLineRunner seedAdminUser(
            UserRepository userRepository,
            RoleRepository roleRepository,
            @Value("${ADMIN_NAME:dev_admin}") String adminName,
            @Value("${ADMIN_USERNAME:admin}") String adminUsername,
            @Value("${ADMIN_PASSWORD:admin}") String adminPassword,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByUsername(adminUsername)) {
                if(!roleRepository.existsByName(BaseRoles.ROLE_ADMIN.name())) {
                    roleRepository.save(RoleEntity.builder().name(BaseRoles.ROLE_ADMIN.name()).build());
                }
                    UserEntity admin = UserEntity.builder()
                            .name(adminName)
                            .username(adminUsername)
                            .password(passwordEncoder.encode(adminPassword))
                            .roles(Set.of(roleRepository.findByName("ROLE_ADMIN")))
                            .build();

                    userRepository.save(admin);
            }
        };
    }
}
