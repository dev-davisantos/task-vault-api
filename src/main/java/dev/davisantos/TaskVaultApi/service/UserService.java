package dev.davisantos.TaskVaultApi.service;

import dev.davisantos.TaskVaultApi.database.model.RoleEntity;
import dev.davisantos.TaskVaultApi.database.model.UserEntity;
import dev.davisantos.TaskVaultApi.database.repository.RoleRepository;
import dev.davisantos.TaskVaultApi.database.repository.UserRepository;
import dev.davisantos.TaskVaultApi.dto.UserRequestDTO;
import dev.davisantos.TaskVaultApi.dto.UserResponseDTO;
import dev.davisantos.TaskVaultApi.exception.NotFoundException;
import dev.davisantos.TaskVaultApi.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserRequestDTO dto) { //Simple creation, not gonna be used so long
        Set<RoleEntity> roles = dto.roleIds().stream()
                .map(role -> roleRepository.findById(role)
                        .orElse(null))
                .collect(Collectors.toSet());

        UserEntity user = userMapper.toEntity(dto);
        user.setRoles(roles);

        return  userMapper.toDto(userRepository.save(user));
    }

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
