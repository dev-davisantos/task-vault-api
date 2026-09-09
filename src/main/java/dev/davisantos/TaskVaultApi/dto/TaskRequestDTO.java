package dev.davisantos.TaskVaultApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequestDTO(
        @NotBlank String title,
        @NotBlank String description,
        String priority,
        @NotNull Long requesterId

) {
}
