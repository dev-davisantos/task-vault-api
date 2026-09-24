package dev.davisantos.TaskVaultApi.service;

import dev.davisantos.TaskVaultApi.config.TokenProvider;
import dev.davisantos.TaskVaultApi.database.model.RoleEntity;
import dev.davisantos.TaskVaultApi.database.model.UserEntity;
import dev.davisantos.TaskVaultApi.database.repository.RoleRepository;
import dev.davisantos.TaskVaultApi.database.repository.UserRepository;
import dev.davisantos.TaskVaultApi.dto.LoginRequestDTO;
import dev.davisantos.TaskVaultApi.dto.TokenResponseDTO;
import dev.davisantos.TaskVaultApi.dto.UserRequestDTO;
import dev.davisantos.TaskVaultApi.exception.InvalidActionException;
import dev.davisantos.TaskVaultApi.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public TokenResponseDTO login(LoginRequestDTO dto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );

        String token = tokenProvider.generateToken(authentication);

        return new TokenResponseDTO(token, tokenProvider.getExpirationTime());
    }

    public void register(UserRequestDTO dto){
        if(userRepository.existsByUsername(dto.username())){
            throw new InvalidActionException("This username already exists, please try another username");
        }

        Set<RoleEntity> roles = dto.roleIds().stream()
                .map(roleId -> roleRepository.findById(roleId).orElseThrow(() -> new NotFoundException("Role not found")))
                .collect(Collectors.toSet());

        UserEntity user = UserEntity.builder()
                .name(dto.name())
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .roles(roles)
                .build();

        userRepository.save(user);
    }

}
