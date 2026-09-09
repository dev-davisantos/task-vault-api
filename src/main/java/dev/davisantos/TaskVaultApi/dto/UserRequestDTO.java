package dev.davisantos.TaskVaultApi.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank String name,
        @NotBlank String username,
        @NotBlank String password
) {
}
