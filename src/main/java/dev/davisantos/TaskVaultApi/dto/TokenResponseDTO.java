package dev.davisantos.TaskVaultApi.dto;

public record TokenResponseDTO(
        String token,
        Long expiresIn
) {
}
