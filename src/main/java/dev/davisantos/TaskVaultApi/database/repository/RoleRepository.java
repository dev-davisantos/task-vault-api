package dev.davisantos.TaskVaultApi.database.repository;

import dev.davisantos.TaskVaultApi.database.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
