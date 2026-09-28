package dev.davisantos.TaskVaultApi.service;

import dev.davisantos.TaskVaultApi.database.model.UserEntity;
import dev.davisantos.TaskVaultApi.database.repository.UserRepository;
import dev.davisantos.TaskVaultApi.dto.UserRequestDTO;
import dev.davisantos.TaskVaultApi.dto.UserResponseDTO;
import dev.davisantos.TaskVaultApi.exception.NotFoundException;
import dev.davisantos.TaskVaultApi.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("This user could not be found")));
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public UserResponseDTO updateUser(Long userId,  UserRequestDTO dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("This user could not be found"));

        user.setName(dto.name());
        user.setUsername(dto.username());
        user.setPassword(dto.password());

        return userMapper.toDto(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
