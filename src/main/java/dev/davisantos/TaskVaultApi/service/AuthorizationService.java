package dev.davisantos.TaskVaultApi.service;

import dev.davisantos.TaskVaultApi.database.model.UserEntity;
import dev.davisantos.TaskVaultApi.database.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("authz")
@RequiredArgsConstructor
public class AuthorizationService {

    private final TaskRepository taskRepository;

    public boolean canUpdate(Authentication auth, Long taskId) {
        if (!(auth.getPrincipal() instanceof UserEntity user)) {
            return false;
        }
        return taskRepository.findById(taskId)
                .map(task -> task.getRequester().getId().equals(user.getId()))
                .orElse(false);
    }

}
