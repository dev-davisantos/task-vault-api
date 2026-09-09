package dev.davisantos.TaskVaultApi.dto;

import java.time.Instant;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        String status,
        String priority,
        UserResponseDTO requester,
        UserResponseDTO owner,
        UserResponseDTO assignedBy,
        Instant assignedAt,
        Instant createdAt,
        Instant updatedAt

) {
}
