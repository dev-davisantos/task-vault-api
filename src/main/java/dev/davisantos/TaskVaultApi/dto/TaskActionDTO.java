package dev.davisantos.TaskVaultApi.dto;

import jakarta.validation.constraints.NotNull;

public record TaskActionDTO(
        @NotNull Long ownerId
) {
}
