package dev.davisantos.TaskVaultApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UserRequestDTO(
        @NotBlank String name,
        @NotBlank String username,
        @NotBlank String password,
        @NotEmpty List<Long> roleIds
) {
}
