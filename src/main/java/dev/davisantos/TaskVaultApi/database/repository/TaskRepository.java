package dev.davisantos.TaskVaultApi.database.repository;

import dev.davisantos.TaskVaultApi.database.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
